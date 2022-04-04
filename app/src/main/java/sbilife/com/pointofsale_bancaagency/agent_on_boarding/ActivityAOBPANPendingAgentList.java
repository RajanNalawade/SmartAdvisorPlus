package sbilife.com.pointofsale_bancaagency.agent_on_boarding;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import sbilife.com.pointofsale_bancaagency.Utility;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

/**
 * Created by O0110 on 14/05/2018.
 */

public class ActivityAOBPANPendingAgentList extends AppCompatActivity {
    private final String NAMESPACE = "http://tempuri.org/";

    private final String METHOD_NAME_AM_BDM_LIST = "getAgentPan_PendingQues_AgentOnboard";

    private ProgressDialog mProgressDialog;

    private CommonMethods mCommonMethods;
    private Context context;
    private String strCIFBDMUserId = "";
    private ListView listviewAOBPANPendingAgentList;

    private ArrayList<ParseXML.AOBPANPendingAgentListValuesModel> lstPendingAgentList = new ArrayList<>();
    private SelectedAdapterPendingAgentList selectedAdapterPendingAgentList;
    private AsyncTaskAOBPANPendingAgentList asyncTaskAOBPANPendingAgentList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.layout_aob_pan_pending_agent_list);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        strCIFBDMUserId = getIntent().getStringExtra("UMCode");
        context = this;
        mCommonMethods = new CommonMethods();

        mCommonMethods.setApplicationToolbarMenu1(this, "Agent Pending List");

        mProgressDialog = new ProgressDialog(context);
        mProgressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

        listviewAOBPANPendingAgentList = (ListView)findViewById(R.id.listviewAOBPANPendingAgentList);

        asyncTaskAOBPANPendingAgentList = new AsyncTaskAOBPANPendingAgentList();
        asyncTaskAOBPANPendingAgentList.execute();

        listviewAOBPANPendingAgentList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                List lstSubMenus = new ArrayList();

                lstSubMenus.add("BSM Questions");

                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_submenu_all);
                dialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setCancelable(false);

                TextView dia_aob_req_upload_title = dialog.findViewById(R.id.dia_aob_req_upload_title);
                dia_aob_req_upload_title.setText("Select Action");

                ImageView dia_aob_req_cancel = dialog.findViewById(R.id.dia_aob_req_cancel);

                RecyclerView dia_aob_req_upload_list = (RecyclerView) dialog.findViewById(R.id.dia_aob_req_upload_list);

                // call the constructor of CustomAdapter to send the reference and data to Adapter
                DialogAdapterAOBRequirementDocs clad = new DialogAdapterAOBRequirementDocs(context, lstSubMenus, i);

                // set a LinearLayoutManager with default horizontal orientation and false value for reverseLayout to show the items from start to end
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                // set LayoutManager to RecyclerView
                dia_aob_req_upload_list.setLayoutManager(linearLayoutManager);
                dia_aob_req_upload_list.setAdapter(clad);
                dia_aob_req_upload_list.setItemAnimator(new DefaultItemAnimator());

                dia_aob_req_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

                return false;
            }
        });
    }

    class AsyncTaskAOBPANPendingAgentList extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String inputpolicylist = "", strBdmListErrorCOde1 = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                String soap_action;

                running = true;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_AM_BDM_LIST);
                request.addProperty("strUM", strCIFBDMUserId);


                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();
                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                try {
                    androidHttpTranport
                            .call(NAMESPACE + METHOD_NAME_AM_BDM_LIST, envelope);
                    Object response = envelope.getResponse();

                    if (!response.toString().contentEquals("<NewDataSet />")) {

                        SoapPrimitive sa;
                        try {
                            sa = (SoapPrimitive) envelope.getResponse();

                            inputpolicylist = sa.toString();

                            ParseXML prsObj = new ParseXML();

                            inputpolicylist = prsObj.parseXmlTag(
                                    inputpolicylist, "NewDataSet");
                            strBdmListErrorCOde1 = inputpolicylist;

                            strBdmListErrorCOde1 = prsObj.parseXmlTag(
                                    strBdmListErrorCOde1, "NewDataSet");
                            strBdmListErrorCOde1 = inputpolicylist;

                            if (strBdmListErrorCOde1 != null) {
                                inputpolicylist = sa.toString();

                                inputpolicylist = new ParseXML()
                                        .parseXmlTag(inputpolicylist,
                                                "NewDataSet");
                                inputpolicylist = new ParseXML()
                                        .parseXmlTag(inputpolicylist,
                                                "ScreenData");
                                strBdmListErrorCOde1 = inputpolicylist;

                                if (strBdmListErrorCOde1 == null) {

                                    inputpolicylist = sa.toString();
                                    inputpolicylist = prsObj.parseXmlTag(
                                            inputpolicylist, "NewDataSet");

                                    List<String> Node = prsObj.parseParentNode(
                                            inputpolicylist, "Table");

                                    List<ParseXML.AOBPANPendingAgentListValuesModel> nodeData = prsObj
                                            .parseNodeAOBPANPendingAgentList(Node);

                                    lstPendingAgentList = new ArrayList<ParseXML.AOBPANPendingAgentListValuesModel>();
                                    lstPendingAgentList.clear();

                                    for (ParseXML.AOBPANPendingAgentListValuesModel node : nodeData) {

                                        lstPendingAgentList.add(node);
                                    }

                                    selectedAdapterPendingAgentList = new SelectedAdapterPendingAgentList(
                                            context,  lstPendingAgentList);
                                    selectedAdapterPendingAgentList.setNotifyOnChange(true);
                                    //registerForContextMenu(listviewAOBPANPendingAgentList);

                                }

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
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (running) {
                if (strBdmListErrorCOde1 == null) {
                    listviewAOBPANPendingAgentList.setAdapter(selectedAdapterPendingAgentList);
                    Utility.setListViewHeightBasedOnChildren(listviewAOBPANPendingAgentList);

                } else {
                    listviewAOBPANPendingAgentList.setVisibility(View.VISIBLE);

                    List<ParseXML.AOBPANPendingAgentListValuesModel> lst;
                    ParseXML.AOBPANPendingAgentListValuesModel node = null;
                    lst = new ArrayList<>();
                    lst.clear();
                    lst.add(node);
                    selectedAdapterPendingAgentList = new SelectedAdapterPendingAgentList(context, lst);
                    selectedAdapterPendingAgentList.setNotifyOnChange(true);
                    listviewAOBPANPendingAgentList.setAdapter(selectedAdapterPendingAgentList);

                    Utility.setListViewHeightBasedOnChildren(listviewAOBPANPendingAgentList);
                }
            } else {
                //servererror();
                mCommonMethods.showMessageDialog(context, mCommonMethods.SERVER_ERROR);
            }
        }
    }

    public class SelectedAdapterPendingAgentList extends ArrayAdapter<ParseXML.AOBPANPendingAgentListValuesModel> {

        // used to keep selected position in ListView

        final List<ParseXML.AOBPANPendingAgentListValuesModel> lst;

        public SelectedAdapterPendingAgentList(Context context,
                                  List<ParseXML.AOBPANPendingAgentListValuesModel> objects) {
            super(context, 0, objects);
            lst = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) this.getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item_aob_pan_pending_agent_list, null);
            }

            TextView textviewAOBPANPendingAgentPANNumberTitle = (TextView) v.findViewById(R.id.textviewAOBPANPendingAgentPANNumber);
            TextView textviewAOBPANPendingAgentFullName = (TextView) v.findViewById(R.id.textviewAOBPANPendingAgentFullName);
            LinearLayout ll_reject_status = v.findViewById(R.id.ll_reject_status);
            TextView textviewAOBPANPendingAgentStatus = (TextView) v.findViewById(R.id.textviewAOBPANPendingAgentStatus);
            TextView textviewAOBPANPendingAgentRemark = (TextView) v.findViewById(R.id.textviewAOBPANPendingAgentRemark);

            Object obj = null;
            boolean i = lst.contains(obj);

            if (!i) {
                textviewAOBPANPendingAgentPANNumberTitle.setText(lst.get(position).getPER_PAN_NO());
                textviewAOBPANPendingAgentFullName.setText(lst.get(position).getPER_FULL_NAME());

                if (lst.get(position).getStatus().equals("")){
                    ll_reject_status.setVisibility(View.GONE);
                }else{
                    ll_reject_status.setVisibility(View.VISIBLE);

                    textviewAOBPANPendingAgentStatus.setText(lst.get(position).getStatus());
                    textviewAOBPANPendingAgentRemark.setText(lst.get(position).getRemark());
                }
            }

            return (v);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (asyncTaskAOBPANPendingAgentList != null) {
            asyncTaskAOBPANPendingAgentList.cancel(true);
        }
    }

    public class DialogAdapterAOBRequirementDocs extends RecyclerView.Adapter<DialogAdapterAOBRequirementDocs.ViewHolderAdapter> {

        private final Context mAdapterContext;
        private List lstAdapterList;
        private int mainPos;

        DialogAdapterAOBRequirementDocs(Context mAdapterContext, List lstAdapterList, int position) {
            this.mAdapterContext = mAdapterContext;
            this.lstAdapterList = lstAdapterList;
            mainPos = position;
        }

        @Override
        public int getItemCount() {
            return lstAdapterList.size();
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
        public void onBindViewHolder(DialogAdapterAOBRequirementDocs.ViewHolderAdapter holder, int position) {

            holder.tvMenuItem.setText(lstAdapterList.get(position).toString());

            holder.tvMenuItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (lstPendingAgentList.size() > 0){
                        String PANNumber = lstPendingAgentList.get(mainPos).getPER_PAN_NO();
                        PANNumber = PANNumber == null ? "" : PANNumber;

                        if (!PANNumber.equals("")) {
                            Intent intent = new Intent(context, ActivityAOBInterviewBSM.class);
                            intent.putExtra("UMCode", strCIFBDMUserId);
                            intent.putExtra("PANNumber",PANNumber);
                            intent.putExtra("enrollment_type", lstPendingAgentList.get(mainPos).getEnrollment_type());
                            startActivity(intent);
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
