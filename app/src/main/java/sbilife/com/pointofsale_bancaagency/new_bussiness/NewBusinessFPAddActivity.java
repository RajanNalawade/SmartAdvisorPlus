package sbilife.com.pointofsale_bancaagency.new_bussiness;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore.MediaColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.ParseXML.XMLHolderBankList;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.SendSmsAsyncTask;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;

public class NewBusinessFPAddActivity extends AppCompatActivity {
    private final String NAMESPACE = "http://tempuri.org/";
    private final String URl = ServiceURL.SERVICE_URL;

    private int RESULT_LOAD_IMAGE = 100;
    private int CAMERA_CAPTURE = 101;

    //private String nb_fp_folderName = "NB_FP";

    private EditText et_nb_fp_customer_name, et_nb_fp_proposal_no, et_nb_fp_micr_code,
            et_nb_fp_mobile_num, et_nb_fp_accnt_no, et_nb_fp_cheque_no,
            et_nb_fp_cheque_date, et_nb_fp_cheque_amount;

    private EditText et_nb_fp_bank_name, et_nb_fp_branch_name;

    //private RadioGroup rg_nb_add;

    private TextView tv_nb_fp_bank, tv_nb_fp_branch;

    private Spinner sp_nb_pay_mode;
    private Spinner sp_nb_pay_type;

    Button btn_new_business_fp_camera;
    Button btn_new_business_fp_gallery;
    Button btn_new_business_fp_view;
    private Button btn_new_business_fp_submit;

    private String cus_name, proposal_no, micr_code, mob_num, account_no, cheque_no,
            cheque_date, cheque_amount, pay_mode, pay_type = "";

    private String bank_name, branch_name, bank_name_selctd = "";

    private DatabaseHelper dbhelper;


    private boolean isMICR;//, bank_name_validation, micrValidation = false;

    private int curr_year, curr_month, curr_day;

    private ProgressDialog pd;

    private String cif_number;
    private String user_type;
    private String user_password;
    private String user_email_id;
    private String user_mob;

    private String save_nb_response;
    private String bank_list_response = "";
    private CommonMethods mCommonMethods;
    private Context context;
    private StorageUtils mStorageUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_business_fp_add);
        dbhelper = new DatabaseHelper(this);
        context = this;
        mCommonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
        et_nb_fp_customer_name = findViewById(R.id.et_nb_fp_customer_name);
        et_nb_fp_proposal_no = findViewById(R.id.et_nb_fp_proposal_no);
        et_nb_fp_micr_code = findViewById(R.id.et_nb_fp_micr_code);

        et_nb_fp_bank_name = findViewById(R.id.et_nb_fp_bank_name);
        et_nb_fp_branch_name = findViewById(R.id.et_nb_fp_branch_name);

        et_nb_fp_mobile_num = findViewById(R.id.et_nb_fp_mobile_num);

        et_nb_fp_accnt_no = findViewById(R.id.et_nb_fp_accnt_no);
        et_nb_fp_cheque_no = findViewById(R.id.et_nb_fp_cheque_no);
        et_nb_fp_cheque_date = findViewById(R.id.et_nb_fp_cheque_date);
        et_nb_fp_cheque_amount = findViewById(R.id.et_nb_fp_cheque_amount);
        et_nb_fp_cheque_amount.addTextChangedListener(amountTextWatcher);

        //rg_nb_add = (RadioGroup) findViewById(R.id.rg_nb_add);
        RadioButton rb_nb_add_yes = findViewById(R.id.rb_nb_add_yes);
        RadioButton rb_nb_add_no = findViewById(R.id.rb_nb_add_no);

        tv_nb_fp_bank = findViewById(R.id.tv_nb_fp_bank);
        tv_nb_fp_branch = findViewById(R.id.tv_nb_fp_branch);

        //Setting current day
        final Calendar c = Calendar.getInstance();
        curr_year = c.get(Calendar.YEAR);
        curr_month = c.get(Calendar.MONTH);
        curr_day = c.get(Calendar.DAY_OF_MONTH);

        /*
         * btn_new_business_fp_camera = (Button)
         * v.findViewById(R.id.btn_new_business_fp_camera);
         * btn_new_business_fp_gallery = (Button)
         * v.findViewById(R.id.btn_new_business_fp_gallery);
         * btn_new_business_fp_view = (Button)
         * v.findViewById(R.id.btn_new_business_fp_view);
         */

        sp_nb_pay_mode = findViewById(R.id.sp_nb_pay_mode);
        sp_nb_pay_type = findViewById(R.id.sp_nb_pay_type);

        btn_new_business_fp_submit = findViewById(R.id.btn_new_business_fp_submit);

        et_nb_fp_proposal_no.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    proposalNumberValidation();
                }
            }
        });

        rb_nb_add_yes.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                isMICR = true;
                et_nb_fp_micr_code.setVisibility(View.VISIBLE);
                tv_nb_fp_bank.setVisibility(View.GONE);
                tv_nb_fp_branch.setVisibility(View.GONE);
                et_nb_fp_bank_name.setVisibility(View.GONE);
                et_nb_fp_branch_name.setVisibility(View.GONE);
            }
        });

        rb_nb_add_no.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                isMICR = false;
                et_nb_fp_micr_code.setVisibility(View.GONE);
                tv_nb_fp_bank.setVisibility(View.VISIBLE);
                tv_nb_fp_branch.setVisibility(View.VISIBLE);
                et_nb_fp_bank_name.setVisibility(View.VISIBLE);
                et_nb_fp_branch_name.setVisibility(View.VISIBLE);
            }
        });

        et_nb_fp_micr_code
                .setOnFocusChangeListener(new OnFocusChangeListener() {
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            micrValidation();
                        }
                    }
                });

        et_nb_fp_mobile_num
                .setOnFocusChangeListener(new OnFocusChangeListener() {

                    public void onFocusChange(View v, boolean hasFocus) {

                        if (!hasFocus) {
                            mobNoValidation();
                        }
                    }
                });

        // Call DatePicker for Cheque Date(dd/mm/yyyy)
        et_nb_fp_cheque_date
                .setOnFocusChangeListener(new OnFocusChangeListener() {
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            DatePickerDialog d = new DatePickerDialog(
                                    context, R.style.AppBaseTheme,
                                    cheque_date_listener, curr_year,
                                    curr_month, curr_day);
                            d.show();

                        }
                    }
                });

        // Call DatePicker for Cheque Date(dd/mm/yyyy)
        et_nb_fp_cheque_date.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DatePickerDialog d = new DatePickerDialog(context,
                        R.style.AppBaseTheme, cheque_date_listener,
                        curr_year, curr_month, curr_day);
                d.show();
            }
        });


        btn_new_business_fp_submit.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                cus_name = et_nb_fp_customer_name.getText().toString().trim();
                proposal_no = et_nb_fp_proposal_no.getText().toString().trim();
                micr_code = et_nb_fp_micr_code.getText().toString().trim();

                bank_name = et_nb_fp_bank_name.getText().toString().trim();
                branch_name = et_nb_fp_branch_name.getText().toString().trim();

                mob_num = et_nb_fp_mobile_num.getText().toString().trim();

                account_no = et_nb_fp_accnt_no.getText().toString().trim();
                cheque_no = et_nb_fp_cheque_no.getText().toString().trim();
                cheque_date = et_nb_fp_cheque_date.getText().toString().trim();
                cheque_amount = et_nb_fp_cheque_amount.getText().toString().trim();

                pay_mode = sp_nb_pay_mode.getSelectedItem().toString();
                pay_type = sp_nb_pay_type.getSelectedItem().toString();

                proposalNumberValidation();

                mobNoValidation();

                if (micrValidation()) {

                    if (cus_name.length() != 0 && proposal_no.length() == 10 && mob_num.length() == 10
                            && account_no.length() != 0 && cheque_no.length() != 0
                            && cheque_date.length() != 0 && cheque_amount.length() != 0
                            && !pay_mode.equals("Select Pay Mode") && !pay_type.equals("Select Pay Type")) {

                        if (bankNameValidation()) {

                            Toast.makeText(context, "Validation succeeded.",
                                    Toast.LENGTH_SHORT).show();

                            //Removing dot if it is a last char from cheque amount
                            if (cheque_amount.charAt(cheque_amount.length() - 1) == '.') {
                                cheque_amount = cheque_amount.substring(0, cheque_amount.length() - 1);
                            }

                            // Decrypting User details except password
                            try {
                                cif_number = SimpleCrypto.decrypt("SBIL",
                                        dbhelper.GetCIFNo());
                                user_type = SimpleCrypto.decrypt("SBIL",
                                        dbhelper.GetUserType());
                                user_email_id = SimpleCrypto.decrypt("SBIL",
                                        dbhelper.GetEmailId());
                                user_mob = SimpleCrypto.decrypt("SBIL",
                                        dbhelper.GetMobileNo());
                                user_password = dbhelper.GetPassword();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            //Checking for duplicate cheque no
                            if (dbhelper.isDuplicateChequeNo(cheque_no)) {

                                if (mCommonMethods.isNetworkConnected(context)) {
                                    // Async task to save NB details
                                    new SyncNBDetails().execute();
                                } else {
                                    savingDetailsToDB("0");
                                    Toast.makeText(context, "Please check your internet connection.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(context,
                                        "Entered cheque number already exist. Please check again.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context,
                                    "Entered Bank Name doesn't exist. Please search again.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(context,
                                "Please fill up all required fields.",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context,
                            "Please fill up all required fields.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*
         * Bank name search button click on edit text
         */
        et_nb_fp_bank_name.setOnTouchListener(new OnTouchListener() {


            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (et_nb_fp_bank_name.getRight() -
                            et_nb_fp_bank_name.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        bank_name = et_nb_fp_bank_name.getText().toString().trim();

                        if (bank_name.length() > 0) {
                            if (mCommonMethods.isNetworkConnected(context)) {

                                // Calling Async task to get Bank Names
                                new GetBankNames().execute();
                            } else {
                                Toast.makeText(context,
                                        "Please check your internet connection.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context,
                                    "Please Enter Bank Name.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        //showBankNames();

                        return true;
                    }
                }
                return false;
            }
        });
    }

    /*
     * Alert Dialog to show bank names
     */
    private void showBankNames(final String[] bankNames) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Select Bank Name");
        //Cancel Dialog
        builder.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.setItems(bankNames, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(context, bankNames[which], Toast.LENGTH_SHORT).show();
                et_nb_fp_bank_name.setText(bankNames[which]);
                //Setting selected bank name
                bank_name_selctd = bankNames[which].trim();
            }
        });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    //Cheque Amount Textwatcher
    private final TextWatcher amountTextWatcher = new TextWatcher() {

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String str_charSequence = String.valueOf(s);

            if (str_charSequence.contains(".")) {

                //Checking number of dots
                if (str_charSequence.length() - str_charSequence.replace(".", "").length() > 1) {

                    Toast.makeText(context, "Please enter proper cheque amount.", Toast.LENGTH_SHORT).show();

                    //Removing extra dot
                    String str = str_charSequence.substring(0, start) + "" + str_charSequence.substring(start + 1);
                    et_nb_fp_cheque_amount.setText(str);
                    et_nb_fp_cheque_amount.setSelection(start);
                }

                //Checking decimal points
                int indexOfDot = str_charSequence.indexOf(".");

                if (indexOfDot != -1) {
                    try {
                        str_charSequence.charAt(indexOfDot + 3);

                        //Removing third decimal point
                        String str = str_charSequence.substring(0, start) + "" + str_charSequence.substring(start + 1);
                        et_nb_fp_cheque_amount.setText(str);
                        et_nb_fp_cheque_amount.setSelection(start);
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }

                }
            } else {
                if (String.valueOf(s).length() > 12) {
                    String str = str_charSequence.substring(0, start) + "" + str_charSequence.substring(start + 1);
                    et_nb_fp_cheque_amount.setText(str);
                    et_nb_fp_cheque_amount.setSelection(start);
                }
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }
    };

    // Saving NB details using Async task
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

            System.out.println("Cus name: " + cus_name);

            System.out.println("NB details: " + proposal_no + ", " + mob_num
                    + ", " + cus_name + ", " + micr_code + ", " + account_no
                    + ", " + cheque_no + ", " + cheque_amount + ", "
                    + cheque_date + ", " + cif_number + ", " + user_type + ", "
                    + user_email_id + ", " + user_password + ", " + user_mob);

            String METHOD_NAME_SAVE_RP_NB_ACK_DTLS = "saveRpNbAckDtls";
            SoapObject request = new SoapObject(NAMESPACE,
                    METHOD_NAME_SAVE_RP_NB_ACK_DTLS);
            request.addProperty("strPolicyNo", "");
            request.addProperty("strProposalNo", proposal_no);
            request.addProperty("StrCustDob", "01/01/1985"); // *********Hard
            // coded
            // DOB*******
            request.addProperty("strMobNo", mob_num);
            request.addProperty("strCustName", cus_name);
            request.addProperty("strMicrCode", micr_code);
            request.addProperty("strAccNo", account_no);
            request.addProperty("strChqqueNo", cheque_no);
            request.addProperty("StrChDate", convertDateFormat(cheque_date));
            request.addProperty("strCheqAmt", cheque_amount);
            request.addProperty("strAdvCode", cif_number);
            request.addProperty("strAdvType", user_type);
            request.addProperty("strPremType", "NB");
            request.addProperty("strCreatedBy", cif_number);

            System.out.println("New details in background NB:" + pay_mode + ", " + bank_name_selctd + ", " + branch_name);

            request.addProperty("strPACHEQUETYPE", pay_type);
            request.addProperty("strPAPAYMODE", pay_mode);

            request.addProperty("strPABANKNM", bank_name_selctd);
            request.addProperty("strPABRANCHNM", branch_name);

            request.addProperty("strPAPAYMENTTYPE", "Initial"); //Hard coded payment type value for NB

            request.addProperty("strEmailId", user_email_id);
            request.addProperty("strMobileNo", user_mob);
            request.addProperty("strAuthKey", user_password);

            System.out.println("User mail id:" + user_email_id);
            System.out.println("User password:" + user_password + ".");

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            mCommonMethods.TLSv12Enable();

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
            try {
                String SOAP_ACTION_SAVE_RP_NB_ACK_DTLS = "http://tempuri.org/saveRpNbAckDtls";
                androidHttpTranport.call(SOAP_ACTION_SAVE_RP_NB_ACK_DTLS,
                        envelope);
                //Object response = envelope.getResponse();

                SoapPrimitive sa = null;

                sa = (SoapPrimitive) envelope.getResponse();

                if (sa != null) {
                    save_nb_response = sa.toString();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (pd.isShowing()) {
                pd.dismiss();
            }

            System.out.println("Response in post: " + save_nb_response);

            if (save_nb_response != null) {
                // Checking response
                if (save_nb_response.equals("1")) {
                    Toast.makeText(context,
                            "Details has been synced successfully.",
                            Toast.LENGTH_SHORT).show();

                    savingDetailsToDB("1");

                    // Send SMS
                    sendSms();
                } else {
                    Toast.makeText(context,
                            "Details has been not synced. Please try again.",
                            Toast.LENGTH_SHORT).show();
                    // savingDetailsToDB("0");
                }
            } else {
                Toast.makeText(context,
                        "Details has been not synced. Please try again.",
                        Toast.LENGTH_SHORT).show();
            }

        }

    }

    //Async Task for Bank Names
    private class GetBankNames extends AsyncTask<Void, Void, Void> {

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

            String METHOD_NAME_GET_BANK_LIST = "getBankList";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_GET_BANK_LIST);
            request.addProperty("strBankName", bank_name.toUpperCase(Locale.ENGLISH));

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            mCommonMethods.TLSv12Enable();

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
            try {
                String SOAP_ACTION_GET_BANK_LIST = "http://tempuri.org/getBankList";
                androidHttpTranport.call(SOAP_ACTION_GET_BANK_LIST,
                        envelope);
                //Object response = envelope.getResponse();

                SoapPrimitive sa = null;

                sa = (SoapPrimitive) envelope.getResponse();

                if (sa != null) {
                    bank_list_response = sa.toString();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (pd.isShowing()) {
                pd.dismiss();
            }

            if (bank_list_response != null) {
                if (bank_list_response.equals("0") || bank_list_response.equals("") || bank_list_response.equals("<NewDataSet />")) {
                    Toast.makeText(context, "Bank Name couldn't found. Please re-type the Bank Name and search again.", Toast.LENGTH_LONG).show();
                } else {
                    ParseXML parseXML = new ParseXML();
                    String[] bankNames;
                    int bankCount = 0;

                    List<XMLHolderBankList> bankList = parseXML.parseNodeElementBankList(parseXML.parseParentNode
                            (parseXML.parseXmlTag(bank_list_response, "NewDataSet"), "Table"));

                    bankNames = new String[bankList.size()];

                    for (XMLHolderBankList bankName : bankList) {
                        bankNames[bankCount] = bankName.getBankName();
                        bankCount++;
                    }
                    System.out.println("Bank count:" + bankCount);

                    showBankNames(bankNames);
                }
            } else {
                Toast.makeText(context, "Bank Name couldn't found. Please re-type the Bank Name and search again.", Toast.LENGTH_LONG).show();
            }
        }

    }

    //MICR Validation
    private boolean micrValidation() {
        if (isMICR) {
            if (micr_code != null) {
                if (micr_code.length() == 9) {
                    bank_name = "";
                    branch_name = "";
                    et_nb_fp_micr_code.setError(null);
                    return true;
                } else {
                    et_nb_fp_micr_code.setError("MICR code require 9 digits.");
                    return false;
                }
            } else {
                return false;
            }

        } else {
            if (bank_name != null && branch_name != null) {
                if (bank_name.length() > 0 && branch_name.length() > 0) {
                    micr_code = "";
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }

        }
    }

    //Bank Name Validation
    private boolean bankNameValidation() {
        if (bank_name_selctd != null) {
            return bank_name_selctd.equals(bank_name);
        } else {
            return false;
        }
    }

    //Proposal Number Validation
    private void proposalNumberValidation() {
        if (proposal_no != null) {
            if (proposal_no.length() < 10) {
                et_nb_fp_proposal_no.setError("Proposal number require 10 digits.");
            } else {
                et_nb_fp_proposal_no.setError(null);
            }
        }
    }

    //Mobile number validation
    private void mobNoValidation() {
        if (et_nb_fp_mobile_num.getText().toString().length() < 10) {
            et_nb_fp_mobile_num
                    .setError("Mobile number require 10 digits.");
        } else {
            et_nb_fp_mobile_num.setError(null);
        }
    }

    // Send SMS
    private void sendSms() {

        try {
            String str_message =

                    "Thank you. Received Cheque No. "
                            + cheque_no
                            + " dated "
                            + cheque_date
                            + " for Rs. "
                            + cheque_amount
                            + " towards proposal number "
                            + proposal_no
                            + ". T&C apply, refer sbilife.co.in. ~ SBI Life";

           /* SmsManager  smsManager = SmsManager.getDefault();

            ArrayList<String> parts = smsManager.divideMessage(str_message);
            smsManager.sendMultipartTextMessage("+91"+mob_num, null, parts, null, null);

            Toast.makeText(context, "SMS has been sent successfully.", Toast.LENGTH_SHORT).show();*/

            if (mCommonMethods.isNetworkConnected(context)) {
                SendSmsAsyncTask sendSmsAsyncTask = new SendSmsAsyncTask(context, mob_num, str_message);
                sendSmsAsyncTask.execute("");
            } else {
                mCommonMethods.showMessageDialog(context, mCommonMethods.NO_INTERNET_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "SMS sending failed.", Toast.LENGTH_SHORT).show();
        }
    }

    // Saving details to DB
    private void savingDetailsToDB(String isSyncd) {
        /*
         * Saving details to DB
         */
        RenewalPremiumNBBean bean = new RenewalPremiumNBBean();
        try {
            bean.setRp_nb_proposal_no(proposal_no);
            bean.setRp_nb_micr(micr_code);
            bean.setRp_nb_cust_name(cus_name);
            bean.setRp_nb_cust_mob(mob_num);

            bean.setRp_nb_bank_name(bank_name_selctd);
            bean.setRp_nb_branch_name(branch_name);

            bean.setRp_nb_accnt_no(account_no);
            bean.setRp_nb_cheque_no(cheque_no);
            bean.setRp_nb_cheque_date(cheque_date);
            bean.setRp_nb_cheque_amt(cheque_amount);

            bean.setRp_nb_pay_mode(pay_mode);
            bean.setRp_nb_pay_type(pay_type);

            bean.setRp_nb_payment_type("Initial"); //Hard coded Payment Type for NB

            bean.setRp_nb_advisor_code(cif_number);
            bean.setRp_nb_advisor_type(user_type);

            bean.setRp_nb_created_date(getCurrentDateInFormat());
            bean.setRp_nb_created_by(cif_number);

            bean.setRp_nb_is_rp("NB");

            long table_row_id = dbhelper.saveNewBusinessFPDetails(bean);
            System.out.println("table_row_id:" + table_row_id);

            if (table_row_id > 0) {
			/*Toast.makeText(context, "Details has been stored in DB.",
					Toast.LENGTH_SHORT).show();*/

                if (isSyncd.equals("1")) {
                    if (dbhelper.updateRenewalPreSyncFlag(table_row_id, "1") > 0) {
					/*Toast.makeText(context,
							"Syncd status has been changed to 1 in DB.",
							Toast.LENGTH_SHORT).show();*/
                    } else {
					/*Toast.makeText(context,
							"Syncd status has not been changed in DB.",
							Toast.LENGTH_SHORT).show();*/
                    }
                }

                // Refresh the activity to update the list view
                Intent i = new Intent(context,
                        NewBusinessFirstPremium.class);
                startActivity(i);
                finish();
            } else {
			/*Toast.makeText(getActivity(),
					"Details has not been saved in mob db.",
					Toast.LENGTH_SHORT).show();*/
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Convert dd/mm/yyyy to mm/dd/yyyy
    private String convertDateFormat(String dateToBeConvert) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    "dd/MM/yyyy");
            Date tempDate = simpleDateFormat.parse(dateToBeConvert);
            SimpleDateFormat outputDateFormat = new SimpleDateFormat(
                    "MM/dd/yyyy");
            System.out.println("Output date is = "
                    + outputDateFormat.format(tempDate));
            return outputDateFormat.format(tempDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean fileValidation() {
        proposal_no = getProposalNumber();
        if (proposal_no != null) {
                // Checking for pdf file
                File file_pdf = mStorageUtils.createFileToAppSpecificDir(context, proposal_no + ".pdf");
                if (file_pdf.exists()) {
                    return true;
                } else {
                    Toast.makeText(context,
                            "Please take document image.", Toast.LENGTH_SHORT)
                            .show();
                    return false;
                }
        } else {
            Toast.makeText(context, "Proposal number cannot be blank.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // Checking whether policy number is blank or not
    private String getProposalNumber() {
        proposal_no = et_nb_fp_proposal_no.getText().toString().trim();
        if (proposal_no.length() != 0) {
            return proposal_no;
        } else {
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMAGE) {
                System.out.println("Gallery image succeeded");

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaColumns.DATA};

                Cursor cursor = context.getContentResolver().query(
                        selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;

                Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

                Matrix matrix = new Matrix();
                matrix.postRotate(getImageOrientation(filePath));
                Bitmap gallery_rotated_bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                storeDocument(gallery_rotated_bitmap, proposal_no);

            } else if (requestCode == CAMERA_CAPTURE) {

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;

                Bitmap bitmap = BitmapFactory.decodeFile(mStorageUtils.createFileToAppSpecificDir(
                        context,proposal_no + ".png").getAbsolutePath(), options);

                Matrix matrix = new Matrix();
                matrix.postRotate(getImageOrientation(mStorageUtils.createFileToAppSpecificDir(
                        context,proposal_no + ".png").getAbsolutePath()));
                Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                storeDocument(rotatedBitmap, proposal_no);
            }
        }
    }

    // Store Document
    private void storeDocument(Bitmap bitmap, String fileName) {

        // Checking for png file
        File file_png = mStorageUtils.createFileToAppSpecificDir(
                context, fileName + ".png");
        if (file_png.exists())
            file_png.delete();

        File file = mStorageUtils.createFileToAppSpecificDir(
                context, fileName + ".pdf");
        if (file.exists()) {
            file.delete();
        }

        try {
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
            Image image = Image.getInstance(outputStream.toByteArray());

            image.setAlignment(Image.MIDDLE);
            image.scaleToFit(550, 400);
            document.add(image);
            document.close();

            outputStream.flush();
            outputStream.close();

            Toast.makeText(context, "Image saved successfully!!",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Problem found while saving image.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private DatePickerDialog.OnDateSetListener cheque_date_listener = new DatePickerDialog.OnDateSetListener() {

        @SuppressWarnings("deprecation")
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            final Calendar c = Calendar.getInstance();

            Calendar selectedDate = new GregorianCalendar(year, monthOfYear,
                    dayOfMonth);
            selectedDate.add(Calendar.HOUR_OF_DAY, c.getTime().getHours());
            selectedDate.add(Calendar.MINUTE, c.getTime().getMinutes());
            selectedDate.add(Calendar.SECOND, c.getTime().getSeconds());

            Calendar minRequiredChequeDate = new GregorianCalendar();
            minRequiredChequeDate.add(Calendar.MONTH, -3);
            minRequiredChequeDate.set(Calendar.HOUR_OF_DAY, 0);
            minRequiredChequeDate.set(Calendar.MINUTE, 0);
            minRequiredChequeDate.set(Calendar.SECOND, 1);

            if (selectedDate.before(minRequiredChequeDate)) {
                Toast.makeText(context,
                        "Cheque date should not be less than 3 months.",
                        Toast.LENGTH_SHORT).show();
                et_nb_fp_cheque_date.setText("");
            } else if (selectedDate.after(getCurrentDate(c))) {
                Toast.makeText(context,
                        "Cheque date should not be future date.",
                        Toast.LENGTH_SHORT).show();
                et_nb_fp_cheque_date.setText("");
            } else {
			/*Toast.makeText(context, "Proper cheque date.",
					Toast.LENGTH_SHORT).show();*/

                //setting selected cheque date
                curr_day = dayOfMonth;
                curr_month = monthOfYear;
                curr_year = year;

                showDOBDate(et_nb_fp_cheque_date, year, monthOfYear + 1,
                        dayOfMonth);
            }
        }
    };

    private void showDOBDate(EditText et, int year, int month, int day) {
        et.setText(new StringBuilder().append((day < 10 ? "0" + (day) : day)).append("/").append((month < 10 ? "0" + (month) : month))
                .append("/").append(year));
//	et.setText(new StringBuilder().append(day).append("/").append(month)
//			.append("/").append(year));
    }

    // Get current date
    @SuppressWarnings("deprecation")
    private Calendar getCurrentDate(Calendar c) {
        curr_year = c.get(Calendar.YEAR);
        curr_month = c.get(Calendar.MONTH);
        curr_day = c.get(Calendar.DAY_OF_MONTH);

        Calendar currentDate = new GregorianCalendar(curr_year, curr_month,
                curr_day);
        currentDate.add(Calendar.HOUR_OF_DAY, c.getTime().getHours());
        currentDate.add(Calendar.MINUTE, c.getTime().getMinutes());
        currentDate.add(Calendar.SECOND, c.getTime().getSeconds());

        return currentDate;
    }

    // Get current date in format dd/mm/yyyy
    private String getCurrentDateInFormat() {
        Calendar c = Calendar.getInstance();

        curr_year = c.get(Calendar.YEAR);
        curr_month = c.get(Calendar.MONTH);
        curr_day = c.get(Calendar.DAY_OF_MONTH);

        return curr_day + "/" + curr_month
                + "/" + curr_year;
    }

    // Rotate Image
    private int getImageOrientation(String imagePath) {
        int rotate = 0;

        try {
            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

}
