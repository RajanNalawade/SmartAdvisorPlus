package sbilife.com.pointofsale_bancaagency.reports.JarClientDemo;/*
package sbilife.com.pointofsale_bancaagency.reports.JarClientDemo;

import android.Manifest;
import android.annotation.TargetApi;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mosambee.lib.ResultData;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

public class MainActivity extends AppCompatActivity {

    static TextView textView;

    String user = "";
    String pswd = "";
    private FrameLayout container;
    Button btnPayment;
    EditText textAMT, textCBAmt, textUser, textPswd;
    private static Handler handler;
    Button buttonABORT;
    private static final int REQUEST_CODE_PERMISSION = 2;
    public static final String SERVICE_URL = "https://sbiluatposservices.sbilife.co.in/service.asmx?wsdl";

    String[] mPermission = {Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN};
    private String transType = "SALE";
    private Button buttonPS;
    static Button button_sendEmail, button_sendSMS;
    static String TransationId = "";

    private static String DEBUG_TAG = "Mpos_ingrate_connectlifeActivity";

    String NAMESPACE = "http://tempuri.org/";
    String URL = "https://sbiluatposservices.sbilife.co.in/service.asmx?wsdl";

    private static final String SOAP_ACTION_saveEzTabPaymentTrans = "http://tempuri.org/savePaymentTrans_cl";
    private static final String METHOD_NAME_saveEzTabPaymentTrans = "savePaymentTrans_cl";

    private AsyncCheckPaymentStatus servicePaymentStatus;
    String strPayment_Status = "";
    Context context = this;
    static ProgressDialog progressDialog;
    Context mContext = this;

    private String QuatationNumber;
    private String currentRecordId = "";
    private String planName = "";
    private String productCode = "";
    private String agentId = "";

    public static String PremiumAmount;
    WebView web;
    String encr_Password = "";
    String decr_Password = "";
    String strEmailId = "";
    String strmobilenumber = "";
    String strPaymentStatus = "";

    StringBuilder inputVal;

    double double_amount = 0;
    String str_amount = "";
    String str_amountFormatted = "";
    String str_AuthCode = "";
    String str_BatchNumber = "";
    ;
    String str_CardType = "";
    String str_CustomerMobile = "";
    String str_CustomerreceiptURL = "";
    String str_Externalrefnum = "";
    String str_InvoiceNumber = "";
    String str_LastFourDigits = "";
    String str_MerchantName = "";
    String str_NameOnCard = "";
    String str_PaymentMode = "";
    String str_ReverseReferenceNumber = "";
    String str_Status = "";
    String str_TimeStamp = "";
    double double_totalAmount = 0;
    String str_totalAmount = "";
    String str_TransactionId = "";
    String str_Error = "";
    String ReasonCode = "";
    static AlertDialog.Builder builder;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("Req Code for permission", "" + requestCode);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 &&
                    grantResults[0] == MockPackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == MockPackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == MockPackageManager.PERMISSION_GRANTED &&
                    grantResults[3] == MockPackageManager.PERMISSION_GRANTED &&
                    grantResults[4] == MockPackageManager.PERMISSION_GRANTED &&
                    grantResults[5] == MockPackageManager.PERMISSION_GRANTED &&
                    grantResults[6] == MockPackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Permissions granted!!!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "Permission not granted!!!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(MainActivity.this);
        builder = new AlertDialog.Builder(context,
                AlertDialog.THEME_HOLO_LIGHT);


        try {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ActivityCompat.checkSelfPermission(this, mPermission[0])
                        != MockPackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(this, mPermission[1])
                                != MockPackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(this, mPermission[2])
                                != MockPackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(this, mPermission[3])
                                != MockPackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(this, mPermission[4])
                                != MockPackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(this, mPermission[5])
                                != MockPackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(this, mPermission[6])
                                != MockPackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, mPermission, REQUEST_CODE_PERMISSION);
                    // If any permission aboe not allowed by user, this condition will execute every tim, else your else part will work
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        container = (FrameLayout) findViewById(R.id.frameContainer);
        textView = (TextView) findViewById(R.id.textView1);
        btnPayment = (Button) findViewById(R.id.button1);
        textAMT = (EditText) findViewById(R.id.textAMT);
        textCBAmt = (EditText) findViewById(R.id.textCBAmt);

        textUser = (EditText) findViewById(R.id.textUser);
        textPswd = (EditText) findViewById(R.id.textPswd);
        buttonABORT = (Button) findViewById(R.id.buttonABORT);
        buttonPS = (Button) findViewById(R.id.buttonPS);
        context = getApplicationContext();
        button_sendEmail = (Button) findViewById(R.id.button_sendEmail);
        button_sendSMS = (Button) findViewById(R.id.button_sendSMS);


        textUser.setText("9768693970");
        textPswd.setText("1234");
        textAMT.setText("1");

        // final String type[] = {"sale", "pwcb", "cbwp"};
        final String type[] = {"sale"};
        Spinner sp = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, R.layout.spinner_item, type);
        sp.setAdapter(spinnerArrayAdapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                transType = type[arg2];
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                transType = type[0];
            }
        });

        //final JARClass jarClass = new JARClass();
        final JARClassImplementation jarClass = new JARClassImplementation();
        jarClass.setContext(context);
        jarClass.setActivity(this);
        handler = new Handler();
        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View paramView) {
                button_sendEmail.setVisibility(View.GONE);
                button_sendSMS.setVisibility(View.GONE);
                TransationId = "";
                user = textUser.getText().toString();
                pswd = textPswd.getText().toString();
                // if (user.length() > 0 && pswd.length() > 0) {
                if (pswd.length() > 0) {
                    */
/*if (transType.equals("cbwp") || transType.equals("pwcb")) {
                        textView.setText(R.string.transaction);
                        jarClass.startProcess(user, pswd, transType, container, textCBAmt.getText().toString(), String.format("%.2f", (Double.parseDouble(textAMT.getText().toString().trim()))));
                    } else*//*


                    if (transType.equals("sale")) {
                        if (!textAMT.getText().toString().equals("")) {
                            if (Double.parseDouble(textAMT.getText().toString()) > 0.00) {
                                textView.setText(R.string.transaction);
                                jarClass.startProcess(user, pswd, transType, container, "1KNA123456", String.format("%.2f", (Double.parseDouble(textAMT.getText().toString().trim()))));
                                //jarClass.getLocation(user,pswd);
                            } else
                                Toast.makeText(getApplicationContext(),
                                        "Amount should be greater than 0",
                                        Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Enter amount",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                } else
                    Toast.makeText(getApplicationContext(), "Enter username and password details",
                            Toast.LENGTH_LONG).show();
            }
        });

        buttonABORT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jarClass.stopProcess();
            }
        });

        buttonPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PrinterScannerActivity.class);
                startActivity(i);
            }
        });
        button_sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jarClass.sendEmail(TransationId, "machindranath.yewale@sbilife.co.in");
            }
        });
        button_sendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                jarClass.sendSMS(TransationId, "9768693970");
            }
        });
    }


    public void setData(final ResultData result) {
        if (result == null) {
            textView.setText("");

        } else {

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText("Result: " + result.getResult()
                                    + "\nReason code: "
                                    + result.getReasonCode() + "\nReason: "
                                    + result.getReason() + "\nTranaction Id: "
                                    + result.getTransactionId()
                                    + "\nTransactin amount: "
                                    + result.getAmount()
                                    + "\nTransactin data: "
                                    + result.getTransactionData());


                            if (result.getResult()) {
                                str_Status = "Success";
                                str_Error = result.getReason();
                                str_amount = result.getAmount();
                                ReasonCode = result.getReasonCode();
                                TransationId = result.getTransactionId();
                                button_sendEmail.setVisibility(View.VISIBLE);
                                button_sendSMS.setVisibility(View.VISIBLE);
                                showMsgDialog("Result: " + result.getResult()
                                        + "\nReason code: "
                                        + result.getReasonCode() + "\nReason: "
                                        + result.getReason() + "\nTranaction Id: "
                                        + result.getTransactionId()
                                        + "\nTransactin amount: "
                                        + result.getAmount()
                                        + "\nTransactin data: "
                                        + result.getTransactionData());
                                strPaymentStatus = "1";
                            }


                        }
                    });
                }
            };
            new Thread(runnable).start();
        }

    }

    public void setCommand(final String command) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(command);
                    }
                });
            }
        };
        new Thread(runnable).start();

    }

    */
/****************************** async of payment status *******************************************//*

    public void check_payment_status() {

        String Message = "Loading. Please wait...";
        progressDialog.setMessage(Message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        progressDialog.setButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        servicePaymentStatus.cancel(true);
                        progressDialog.dismiss();
                    }
                });

        progressDialog.setMax(100);
        progressDialog.show();

        servicePaymentStatus = new AsyncCheckPaymentStatus();
        servicePaymentStatus.execute();

    }

    public class AsyncCheckPaymentStatus extends
            AsyncTask<String, Void, String> {
        private volatile boolean running = true;

        @Override
        protected String doInBackground(String... param) {
            // TODO Auto-generated method stub
            // Get Channel Detail

            running = true;

            try {
                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_saveEzTabPaymentTrans);

                request.addProperty("strSource", "MOSAMBI");
                request.addProperty("quNo", "123456789");
                request.addProperty("orderNo", str_BatchNumber);
                request.addProperty("grossPremAMT", str_amount);
                request.addProperty("txnRefNo", TransationId);
                request.addProperty("bankCode", "00");
                request.addProperty("strTime", str_TimeStamp);
                request.addProperty("strStatus", str_Status);
                request.addProperty("authStatus", str_AuthCode);
                request.addProperty("strErrDesc", str_Error);
             */
/*       request.addProperty("strCARD_TYPE", str_CardType);
                    request.addProperty("strINVIOICE_NO", str_InvoiceNumber);
                    request.addProperty("strMERCHANT_NAME", str_MerchantName);
                    request.addProperty("strNAME_ON_CARD", str_NameOnCard);
                    request.addProperty("strPAYMENT_MODE", str_PaymentMode);
                    request.addProperty("strEmailId", email_Id);
                    request.addProperty("strMobileNo", mobile_No);
                    request.addProperty("strAuthKey", encr_Password.trim());*//*

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                allowAllSSL();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(
                        URL);

                androidHttpTranport.call(SOAP_ACTION_saveEzTabPaymentTrans,
                        envelope);
                Object response = envelope.getResponse();

                strPayment_Status = response.toString();

            } catch (Exception e) {
                e.printStackTrace();
                running = false;

                return "Server not responding...";
            }


            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (progressDialog.isShowing())
                progressDialog.dismiss();

            if (running != false) {

                if (strPayment_Status.equals("1")) {
                        DialogDisplay("Payment Success", "Thanks For Payment");

                } else {
                    DialogDisplay("Data Not Sync", "Please Try Again");
                }
            } else {
                DialogDisplay("Not Sync", "Server not Responding.. please try Again");


            }
        }

        private boolean isNetworkConnected() {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if (ni != null && ni.isConnected()) {
                // There are active networks.
                return true;
            } else
                return false;
        }

    }

    */
/******************** SSL certificate start *****************************************************************//*

    private static TrustManager[] trustManagers;

    public static class _FakeX509TrustManager implements
            javax.net.ssl.X509TrustManager {
        private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[]{};

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        public boolean isClientTrusted(X509Certificate[] chain) {
            return (true);
        }

        public boolean isServerTrusted(X509Certificate[] chain) {
            return (true);
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return (_AcceptedIssuers);
        }
    }

    public static void allowAllSSL() {
        javax.net.ssl.HttpsURLConnection
                .setDefaultHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });

        javax.net.ssl.SSLContext context = null;

        if (trustManagers == null) {
            trustManagers = new TrustManager[]{new _FakeX509TrustManager()};
        }

        try {
            context = javax.net.ssl.SSLContext.getInstance("TLS");
            context.init(null, trustManagers, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            Log.e("allowAllSSL", e.toString());
        } catch (KeyManagementException e) {
            Log.e("allowAllSSL", e.toString());
        }
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(context
                .getSocketFactory());
    }

    // protected void showMsgDialog(String msg) {
    // AlertDialog alertDialog;
    // alertDialog = new AlertDialog.Builder(this).create();
    // alertDialog.setMessage(msg);
    // alertDialog.setButton("Ok", new OnClickListener() {
    // @Override
    // public void onClick(DialogInterface dialog, int which) {
    // finish();
    // dialog.dismiss();
    // }
    // });
    // alertDialog.setCancelable(false);
    // alertDialog.show();
    // }
    public void showMsgDialog(String Message) {
        builder.setTitle("Payment Details");
        builder.setMessage(Message);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new OkOnClickListener());
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private final class OkOnClickListener implements
            DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (strPaymentStatus.equalsIgnoreCase("1")) {
                check_payment_status();

            } else {
                DialogDisplay("Payment Failure","Sorry, Your Payment process is Failure, Try Again");

            }

        }
    }
    private final class CancelOnClickListener implements
            DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            dialog.dismiss();;
        }
    }

    public void DialogDisplay(String Title, String Message) {
        builder.setTitle(Title);
        builder.setMessage(Message);
        builder.setCancelable(true);
        builder.setPositiveButton("Ok", new OkOnClickListener());
        builder.setPositiveButton("Cancel", new CancelOnClickListener());
        AlertDialog dialog = builder.create();
        dialog.show();

    }

}
*/
