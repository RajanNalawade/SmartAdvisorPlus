package sbilife.com.pointofsale_bancaagency.new_bussiness;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.SendSmsAsyncTask;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;

public class NewBusinessFPViewActivity extends AppCompatActivity {


    private DatabaseHelper dbHelper;

    private List<HashMap<String, String>> hm_rp_details = null;
    private List<RenewalPremiumNBBean> beans = null;

    private List<String> isSyncList = null;
    private RenewalPremiumNBBean obj_bean;

    private ProgressDialog pd;

    private String cif_number;
    private String user_type;
    private String user_password;
    private String user_email_id;
    private String user_mob;

    private String save_rp_nb_response = "";
    private Context context;
    private CommonMethods mCommonMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_business_fp_view);

        context = this;
        dbHelper = new DatabaseHelper(context);
        mCommonMethods = new CommonMethods();

        hm_rp_details = new ArrayList<HashMap<String, String>>();

        beans = new ArrayList<RenewalPremiumNBBean>();

        beans = dbHelper.getRenewalPremiumNBDetails("NB");
        if (beans != null) {
            isSyncList = new ArrayList<String>();
            for (RenewalPremiumNBBean bean : beans) {
                HashMap<String, String> hm_rp_detail = new HashMap<String, String>();

                isSyncList.add(bean.getRp_nb_isSync());

                try {
                    hm_rp_detail.put("proposal_no", "Proposal No. ");
                    hm_rp_detail.put("proposal_no_val", bean.getRp_nb_proposal_no());
                    hm_rp_detail.put("cus_name", bean.getRp_nb_cust_name());
                    hm_rp_detail.put("acc_no", bean.getRp_nb_accnt_no());
                    hm_rp_detail.put("cheque_no", bean.getRp_nb_cheque_no());
                    hm_rp_detail.put("cheque_date", bean.getRp_nb_cheque_date());
                    hm_rp_detail.put("cheque_amt", bean.getRp_nb_cheque_amt());
                    hm_rp_detail.put("pay_mode", bean.getRp_nb_pay_mode());
                    hm_rp_detail.put("pay_type", bean.getRp_nb_pay_type());
                } catch (Exception e) {

                }

                hm_rp_details.add(hm_rp_detail);
            }

            String[] str_from = {"proposal_no", "proposal_no_val", "cus_name", "acc_no", "cheque_no",
                    "cheque_date", "cheque_amt", "pay_mode", "pay_type"};
            int[] in_to = {R.id.tv_policyProposalNo, R.id.tv_policyProposalNo_val, R.id.tv_rp_nb_cus_name, R.id.tv_rp_nb_acc_no,
                    R.id.tv_rp_nb_cheque_no, R.id.tv_rp_nb_cheque_date,
                    R.id.tv_rp_nb_cheque_amt, R.id.tv_rp_nb_pay_mode, R.id.tv_rp_nb_pay_type};

            SimpleAdapter adapter = new SimpleAdapter(context,
                    hm_rp_details, R.layout.renewal_premium_nb_items, str_from,
                    in_to);
            ListView listView = findViewById(R.id.lv_new_business_fp);
            listView.setAdapter(adapter);

            listView.setOnItemLongClickListener(new OnItemLongClickListener() {
                public boolean onItemLongClick(AdapterView<?> parent, View v,
                                               int pos, long id) {

                    String isSync = isSyncList.get(pos);
					/*Toast.makeText(context, "isSync: " + isSync,
							Toast.LENGTH_SHORT).show();*/

                    obj_bean = beans.get(pos);

                    alertDialog(isSync, obj_bean);

                    return false;
                }
            });
        } else {
            Toast.makeText(context, "New Bussiness Premium details are empty.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    // Alert dialog
    private void alertDialog(final String isSync, final RenewalPremiumNBBean bean) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(
                context);
        builder.setTitle("Smart Advisor");
        builder.setMessage("Choose your option");
        builder.setCancelable(true);

        builder.setPositiveButton("Sync", new
                DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        if (isSync.equals("0")) {
                            //Decrypting User details except password
                            try {
                                cif_number = SimpleCrypto.decrypt("SBIL", dbHelper.GetCIFNo());
                                user_type = SimpleCrypto.decrypt("SBIL", dbHelper.GetUserType());
                                user_email_id = SimpleCrypto.decrypt("SBIL", dbHelper.GetEmailId());
                                user_mob = SimpleCrypto.decrypt("SBIL", dbHelper.GetMobileNo());
                                user_password = dbHelper.GetPassword();

                                System.out.println("User encyptd values:" + cif_number + ", " + user_type + ", " + user_email_id + ", " + user_mob + ", " + user_password);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            //Checking internet connection
                            if (mCommonMethods.isNetworkConnected(context)) {
                                //Async task to sync RP details
                                new SyncNBDetails().execute();
                            } else {
                                Toast.makeText(context, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
                            }
                        } else if (isSync.equals("1")) {
                            Toast.makeText(context, "Details already syncd.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        builder.setNegativeButton("Edit",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        if (isSync.equals("0")) {

                            Intent i = new Intent(context,
                                    NewBusinessFPEdit.class);

                            try {
                                i.putExtra("id", bean.getRp_rp_nb_id());
                                i.putExtra("proposal_no",
                                        bean.getRp_nb_proposal_no());
                                i.putExtra(
                                        "accnt_no",
                                        bean.getRp_nb_accnt_no());
                                i.putExtra(
                                        "cheque_no",
                                        bean.getRp_nb_cheque_no());
                                i.putExtra("cheque_date",
                                        bean.getRp_nb_cheque_date());
                                i.putExtra("cheque_amount",
                                        bean.getRp_nb_cheque_amt());

                                i.putExtra("micr", bean.getRp_nb_micr());
                                i.putExtra("bank_name", bean.getRp_nb_bank_name());
                                i.putExtra("branch_name", bean.getRp_nb_branch_name());

                                i.putExtra("pay_mode", bean.getRp_nb_pay_mode());
                                i.putExtra("pay_type", bean.getRp_nb_pay_type());

                                i.putExtra("payment_type", bean.getRp_nb_payment_type());

                                i.putExtra("mob_no", bean.getRp_nb_cust_mob());


                                context.startActivity(i);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else if (isSync.equals("1")) {
                            Toast.makeText(context,
                                    "Details already syncd.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        builder.setNeutralButton("Delete",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        if (isSync.equals("0")) {
                            int j = dbHelper.setDeleteFlagRPNB(bean, "1"); // 0
                            // -
                            // without
                            // deleted
                            // flag,
                            // 1
                            // -
                            // with
                            // deleted
                            // flag.
                            if (j > 0) {
                                Toast.makeText(
                                        context,
                                        "Details has been deleted successfully.",
                                        Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(context, NewBusinessFirstPremium.class);
                                i.putExtra("edit", true);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(
                                        context,
                                        "Problem occured while deleting details.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else if (isSync.equals("1")) {
                            Toast.makeText(context,
                                    "Details already syncd.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //Async task to save NB details
    private class SyncNBDetails extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Loading");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (obj_bean != null) {
                String METHOD_NAME_SAVE_RP_NB_ACK_DTLS = "saveRpNbAckDtls";
                String NAMESPACE = "http://tempuri.org/";
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_SAVE_RP_NB_ACK_DTLS);
                request.addProperty("strPolicyNo", "");
                request.addProperty("strProposalNo", obj_bean.getRp_nb_proposal_no());
                request.addProperty("StrCustDob", "01/01/1985");  //Harded code dob
                request.addProperty("strMobNo", obj_bean.getRp_nb_cust_mob());
                request.addProperty("strCustName", obj_bean.getRp_nb_cust_name());
                request.addProperty("strMicrCode", obj_bean.getRp_nb_micr());
                request.addProperty("strAccNo", obj_bean.getRp_nb_accnt_no());
                request.addProperty("strChqqueNo", obj_bean.getRp_nb_cheque_no());
                request.addProperty("StrChDate", convertDateFormat(obj_bean.getRp_nb_cheque_date()));
                request.addProperty("strCheqAmt", obj_bean.getRp_nb_cheque_amt());
                request.addProperty("strAdvCode", cif_number);
                request.addProperty("strAdvType", user_type);
                request.addProperty("strPremType", "NB");
                request.addProperty("strCreatedBy", cif_number);

                System.out.println("New details in background sync NB:" + obj_bean.getRp_nb_pay_mode() + ", " + obj_bean.getRp_nb_bank_name() + ", " + obj_bean.getRp_nb_branch_name() + ", " + obj_bean.getRp_nb_pay_type());

                request.addProperty("strPACHEQUETYPE", obj_bean.getRp_nb_pay_type());
                request.addProperty("strPAPAYMODE", obj_bean.getRp_nb_pay_mode());

                request.addProperty("strPABANKNM", obj_bean.getRp_nb_bank_name());
                request.addProperty("strPABRANCHNM", obj_bean.getRp_nb_branch_name());

                request.addProperty("strPAPAYMENTTYPE", "Initial"); //Hard coded payment type value for NB

                request.addProperty("strEmailId", user_email_id);
                request.addProperty("strMobileNo", user_mob);
                request.addProperty("strAuthKey", user_password);

                SoapSerializationEnvelope envelope =
                        new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                String URl = ServiceURL.SERVICE_URL;
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {
                    String SOAP_ACTION_SAVE_RP_NB_ACK_DTLS = "http://tempuri.org/saveRpNbAckDtls";
                    androidHttpTranport.call(SOAP_ACTION_SAVE_RP_NB_ACK_DTLS, envelope);

                    SoapPrimitive sa = null;

                    sa = (SoapPrimitive) envelope.getResponse();

                    save_rp_nb_response = sa.toString();

                    System.out.println("save_rp_nb_response:" + save_rp_nb_response + ".end");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("bean is null.");
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (pd.isShowing()) {
                pd.dismiss();
            }

            if (save_rp_nb_response != null) {
                //Checking response
                if (save_rp_nb_response.equals("1")) {
                    Toast.makeText(context, "Details has been synced successfully.", Toast.LENGTH_SHORT).show();

                    //Send SMS
                    sendSms();

                    if (dbHelper.updateRenewalPreSyncFlag(obj_bean.getRp_rp_nb_id(), "1") > 0) {
                        /*Toast.makeText(context, "Syncd status has been changed to 1 in mob db.", Toast.LENGTH_SHORT).show();*/
                        // Refresh the activity to update the list view
                        Intent i = new Intent(context,
                                NewBusinessFirstPremium.class);
                        i.putExtra("edit", true);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(context, "Syncd status has not been changed in mob db.", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(context, "Details has been not synced. Please try again.", Toast.LENGTH_SHORT).show();
                    //savingDetailsToDB("0");
                }
            } else {
                Toast.makeText(context, "Details has been not synced. Please try again.", Toast.LENGTH_SHORT).show();
            }


            System.out.println("save_rp_nb_response inside post: " + save_rp_nb_response + ".end");
        }

    }

    //Send SMS
    private void sendSms() {

        try {
			    /*SmsManager  smsManager = SmsManager.getDefault();
			    
			    ArrayList<String> parts = smsManager.divideMessage("Thank you. Received Cheque No. "+obj_bean.getRp_nb_cheque_no()+" dated "+obj_bean.getRp_nb_cheque_date()
			    		+" for Rs. "+obj_bean.getRp_nb_cheque_amt()+" towards proposal number "+obj_bean.getRp_nb_proposal_no()
			    		+". T&C apply, refer sbilife.co.in. ~ SBI Life");
			    	
			    smsManager.sendMultipartTextMessage("+91"+obj_bean.getRp_nb_cust_mob(), null, parts, null, null);		    
			    	
			    Toast.makeText(context, "SMS has been sent successfully.", Toast.LENGTH_SHORT).show();*/

            String str_message = "Thank you. Received Cheque No. " + obj_bean.getRp_nb_cheque_no() + " dated " + obj_bean.getRp_nb_cheque_date()
                    + " for Rs. " + obj_bean.getRp_nb_cheque_amt() + " towards proposal number " + obj_bean.getRp_nb_proposal_no()
                    + ". T&C apply, refer sbilife.co.in. ~ SBI Life";

            if (mCommonMethods.isNetworkConnected(context)) {
                SendSmsAsyncTask sendSmsAsyncTask = new SendSmsAsyncTask(context, obj_bean.getRp_nb_cust_mob(), str_message);
                sendSmsAsyncTask.execute();
            } else {
                mCommonMethods.showMessageDialog(context, mCommonMethods.NO_INTERNET_MESSAGE);
            }
        } catch (Exception e) {
            Toast.makeText(context, "SMS sending failed.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    //Convert dd/mm/yyyy to mm/dd/yyyy
    private String convertDateFormat(String dateToBeConvert) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date tempDate = simpleDateFormat.parse(dateToBeConvert);
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("MM/dd/yyyy");
            System.out.println("Output date is = " + outputDateFormat.format(tempDate));
            return outputDateFormat.format(tempDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
