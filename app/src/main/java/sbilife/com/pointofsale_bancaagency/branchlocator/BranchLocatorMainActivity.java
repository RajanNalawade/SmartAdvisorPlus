package sbilife.com.pointofsale_bancaagency.branchlocator;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;


public class BranchLocatorMainActivity extends AppCompatActivity {

    private Button btn_getlocation;// ,btn_back;
    private Context mContext;
    // private String address="";
    private  ProgressDialog progressDialog;
    private ParseXML objParse;
    private AlertDialog.Builder showAlert;

    private final String NAMESPACE = "http://tempuri.org/";
    private final String URL = ServiceURL.SERVICE_URL;

    private AutoCompleteTextView spnr_city;
    private Spinner spnr_state;
    private ArrayList<String> resArray;
    private CommonMethods mCommonMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.branch_locator_main_layout);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        new CommonMethods().setApplicationToolbarMenu(this, "Branch Locator");

        mCommonMethods = new CommonMethods();
        resArray = new ArrayList<>();


        spnr_state = findViewById(R.id.state);
        spnr_city = findViewById(R.id.city);
        objParse = new ParseXML();
        showAlert = new AlertDialog.Builder(this, ProgressDialog.THEME_HOLO_LIGHT);

        String[] stateList = {"Select State", "ANDHRA PRADESH",
                "ARUNACHAL PRADESH", "ASSAM", "BIHAR", "CHATTISGARH", "GOA",
                "GUJARAT", "HARYANA", "HIMACHAL PRADESH", "JAMMU AND KASHMIR",
                "JHARKHAND", "KARNATAKA", "KERALA", "LAKSHADEEP",
                "MADHYAPRADESH", "MAHARASHTRA", "MANIPUR", "MEGALAYA",
                "MIZORAM", "PONDICHERRY", "PUNJAB", "RAJASTHAN", "SIKKIM",
                "TAMIL NADU", "TRIPURA", "UTTAR PRADESH", "UTTARAKHAND",
                "WEST BENGAL", "ORISSA", "CHANDIGARH", "NAGALAND",
                "DADRA AND NAGAR HAVELI", "DAMAN AND DIU", "DELHI", "OTHERS",
                "ANDAMAN AND NICOBAR", "TELANGANA"};
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, stateList);
        stateAdapter.setDropDownViewResource(R.layout.spinner_item);
        spnr_state.setAdapter(stateAdapter);
        stateAdapter.notifyDataSetChanged();

        spnr_city.setEnabled(false);
        spnr_city.setClickable(false);

        btn_getlocation = findViewById(R.id.getlocation);
        // btn_back=(Button)findViewById(R.id.back);

        spnr_state.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                       long id) {

                spnr_city.setEnabled(false);
                spnr_city.setClickable(false);

                String state = spnr_state.getSelectedItem().toString();
                if (state.equalsIgnoreCase("Select State")) {

                } else {
                    addListenerOnStateClick(state);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        mContext = this;
        btn_getlocation.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mCommonMethods.hideKeyboard(btn_getlocation, mContext);
                if (valState() && valCity()) {
                    String state = spnr_state.getSelectedItem().toString();
                    String city = spnr_city.getText().toString();

                    addListenerOnSubmit(state, city);
                }
                // Intent intent = new
                // Intent(getApplicationContext(),BranchLocatorMapActivity.class);
                // startActivity(intent);
            }
        });

		/*
         * btn_back.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { // TODO Auto-generated
		 * method stub setResult(RESULT_OK); finish(); } });
		 */

    }

    private void addListenerOnSubmit(String state, String city) {

        LocationManager locationmanager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);

        if (locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            String METHOD_NAME_Map = "getBranchInfo";
            String SOAP_ACTION_Map = "http://tempuri.org/getBranchInfo";
            AsynLoadMap objAsynLoadMap = new AsynLoadMap(mContext,
                    progressDialog, NAMESPACE, URL, SOAP_ACTION_Map,
                    METHOD_NAME_Map, state, city);
            objAsynLoadMap.execute();
        } else {
            mCommonMethods.showGPSDisabledAlertToUser(mContext);
        }
    }

    private void addListenerOnStateClick(String state) {
        String METHOD_NAME_city = "getCityName";
        String SOAP_ACTION_City = "http://tempuri.org/getCityName";
        AsynLoadCity objAsynLoadMap = new AsynLoadCity(mContext,
                progressDialog, NAMESPACE, URL, SOAP_ACTION_City,
                METHOD_NAME_city, state);
        objAsynLoadMap.execute();
    }

    class AsynLoadMap extends AsyncTask<String, Void, String> {
        final Context mContext;
        ProgressDialog progressDialog = null;
        String NAMESPACE = "";
        String URL = "";
        String SOAP_ACTION = "";
        String METHOD_NAME = "";
        String state = "";
        String city = "";
        String output;

        AsynLoadMap(Context mContext, ProgressDialog progressDialog,
                    String NAMESPACE, String URL, String SOAP_ACTION,
                    String METHOD_NAME, String state, String city) {
            // TODO Auto-generated constructor stub

            this.NAMESPACE = NAMESPACE;
            this.URL = URL;
            this.SOAP_ACTION = SOAP_ACTION;
            this.METHOD_NAME = METHOD_NAME;
            this.mContext = mContext;
            this.progressDialog = progressDialog;
            this.state = state;
            this.city = city;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            /*
			 * progressDialog =
			 * ProgressDialog.show(BranchLocatorMainActivity.this,
			 * "Please wait... ", "Loading", true, true);
			 */

            progressDialog = new ProgressDialog(mContext,
                    ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Please wait...";
            progressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>"
                    + Message + "<b></font>"));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Loading");
            progressDialog.setMax(100);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            if (mCommonMethods.isNetworkConnected(mContext)) {
                try {

                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                    request.addProperty("strState", state);
                    request.addProperty("strCity", city);
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                            SoapEnvelope.VER11);
                    // Enable this envelope if service is written in dot net
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(
                            URL);

                    // allowAllSSL();
                    mCommonMethods.TLSv12Enable();

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    androidHttpTransport.call(SOAP_ACTION, envelope);
                    SoapPrimitive response = (SoapPrimitive) envelope
                            .getResponse();

                    // ParseXML prsObj = new ParseXML();
                    String result = response.toString();
                    // System.out.println("result " + result.toString());
                    output = objParse.parseXmlTag(result, "PolicyDetails");
                    if (output == null)
                        return "false";
                    else
                        return output;

                } catch (Exception e) {
                    e.printStackTrace();
                    return "Server not Found. Please try after some time.";
                }
            } else
                return "Please Activate Internet connection";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();

                if (!result.equals("false")) {

                    AsynGetLatLang objAsynLoadMap = new AsynGetLatLang(
                            mContext, progressDialog, output);
                    objAsynLoadMap.execute();
                } else
                    Toast.makeText(mContext, "No Branch Found",
                            Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                e.getMessage();
            }

        }

    }

    class AsynGetLatLang extends AsyncTask<String, Void, String> {
        final Context mContext;
        ProgressDialog progressDialog = null;
        String Lati, Longi;
        StringBuilder retVal;
        int size;
        final String res;

        AsynGetLatLang(Context mContext, ProgressDialog progressDialog,
                       String result) {
            // TODO Auto-generated constructor stub

            res = result;
            this.mContext = mContext;
            this.progressDialog = progressDialog;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            retVal = new StringBuilder();
            progressDialog = new ProgressDialog(mContext,
                    ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Please wait...";
            progressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>"
                    + Message + "<b></font>"));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Loading");
            progressDialog.setMax(100);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            if (mCommonMethods.isNetworkConnected(mContext)) {

                try {
                    Geocoder geocoder = new Geocoder(mContext,
                            Locale.getDefault());
                    try {
                        ArrayList<String> strRes = objParse
                                .parseXmlTagMultiple(res, "Table");
                        retVal.append("<Map>");
                        size = strRes.size();
                        for (int i = 0; i < strRes.size(); i++) {

                            String BR_NAME = objParse.parseXmlTag(
                                    strRes.get(i), "BR_NAME");
                            String BR_ADD1 = objParse.parseXmlTag(
                                    strRes.get(i), "BR_ADD1");
                            String BR_ADD2 = objParse.parseXmlTag(
                                    strRes.get(i), "BR_ADD2");
                            String BR_ADD3 = objParse.parseXmlTag(
                                    strRes.get(i), "BR_ADD3");
                            String BR_PIN_CD = objParse.parseXmlTag(
                                    strRes.get(i), "BR_PIN_CD");
                            String TEL_NO = objParse.parseXmlTag(strRes.get(i),
                                    "TEL_NO");

                            if (BR_PIN_CD != null) {
                                @SuppressWarnings("rawtypes")
                                List addressList = geocoder
                                        .getFromLocationName(BR_PIN_CD, 1);
                                if (addressList != null
                                        && addressList.size() > 0) {
                                    Address address = (Address) addressList
                                            .get(0);
                                    Lati = address.getLatitude() + "";
                                    Longi = address.getLongitude() + "";

                                }

                                retVal.append("<Row").append(i).append(">");
                                retVal.append("<BR_NAME").append(i).append(">").append(BR_NAME).append("</BR_NAME").append(i).append(">");
                                retVal.append("<BR_ADD1").append(i).append(">").append(BR_ADD1).append("</BR_ADD1").append(i).append(">");
                                retVal.append("<BR_ADD2").append(i).append(">").append(BR_ADD2).append("</BR_ADD2").append(i).append(">");
                                retVal.append("<BR_ADD3").append(i).append(">").append(BR_ADD3).append("</BR_ADD3").append(i).append(">");
                                retVal.append("<BR_PIN_CD").append(i).append(">").append(BR_PIN_CD).append("</BR_PIN_CD").append(i).append(">");
                                retVal.append("<TEL_NO").append(i).append(">").append(TEL_NO).append("</TEL_NO").append(i).append(">");
                                retVal.append("<Latitude").append(i).append(">").append(Lati).append("</Latitude").append(i).append(">");
                                retVal.append("<Longitude").append(i).append(">").append(Longi).append("</Longitude").append(i).append(">");
                                retVal.append("</Row").append(i).append(">");
                            }
                        }
                        retVal.append("</Map>");

                        System.out.println(retVal.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return "Server not Found. Please try after some time.";
                }

            } else
                return "Please Activate Internet connection";

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();

                Intent intent = new Intent(getApplicationContext(),
                        BranchLocatorMapFragmentActivity.class);
                intent.putExtra("retVal", retVal.toString());
                intent.putExtra("size", size);

                startActivity(intent);

            } catch (Exception e) {
                e.getMessage();
            }

        }

    }

    class AsynLoadCity extends AsyncTask<String, Void, String> {
        final Context mContext;
        ProgressDialog progressDialog = null;
        String NAMESPACE = "";
        String URL = "";
        String SOAP_ACTION = "";
        String METHOD_NAME = "";
        String state = "";
        String strResponse = "";

        AsynLoadCity(Context mContext, ProgressDialog progressDialog,
                     String NAMESPACE, String URL, String SOAP_ACTION,
                     String METHOD_NAME, String state) {
            this.NAMESPACE = NAMESPACE;
            this.URL = URL;
            this.SOAP_ACTION = SOAP_ACTION;
            this.METHOD_NAME = METHOD_NAME;
            this.mContext = mContext;
            this.progressDialog = progressDialog;
            this.state = state;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            progressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Please wait...";
            progressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.setTitle(Html.fromHtml("<font color='#00a1e3'><b>Loading Cities<b></font>"));
            progressDialog.setMax(100);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            if (mCommonMethods.isNetworkConnected(mContext)) {
                try {

                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                    request.addProperty("strState", state);
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                            SoapEnvelope.VER11);
                    // Enable this envelope if service is written in dot net
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(
                            URL);

                    // allowAllSSL();
                    mCommonMethods.TLSv12Enable();

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    androidHttpTransport.call(SOAP_ACTION, envelope);
                    SoapPrimitive response = (SoapPrimitive) envelope
                            .getResponse();

                    strResponse = response.toString();

                    return "true";

                } catch (Exception e) {
                    e.printStackTrace();
                    return "Server not Found. Please try after some time.";
                }
            } else
                return "Please Activate Internet connection";
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {

                if (progressDialog.isShowing())
                    progressDialog.dismiss();

                if (result.equals("true")) {
                    spnr_city.setEnabled(true);
                    spnr_city.setClickable(true);
                    spnr_city.setText("");
                    ParseXML objParse = new ParseXML();
                    String res = objParse.parseXmlTag(strResponse,
                            "PolicyDetails");

                    resArray = objParse.parseXmlTagMultiple(res, "CI_NAME");

                    ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(
                            BranchLocatorMainActivity.this, R.layout.spinner_item,
                            resArray);
                    spnr_city.setThreshold(1);// will start working from first
                    // character
                    spnr_city.setAdapter(cityAdapter);// s
                } else {
                    Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.getMessage();
            }

        }

    }

    // validation of policy term
    private boolean valState() {
        String error = "";
        if (spnr_state.getSelectedItem().toString().equals("Select State")) {
            error = "Please Select state.";
        }

        if (!error.equals("")) {
            showAlert.setMessage(Html.fromHtml("<font color='#00a1e3'>" + error + "</font>"));
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // TODO Auto-generated method stub

                        }
                    });
            showAlert.show();
            return false;
        }
        return true;
    }

    private boolean valCity() {

        String error = "";
        if (spnr_city.getText().toString().equals("")) {
            error = "Please Select city.";
        } else if (!resArray.contains(spnr_city.getText().toString())) {
            error = "Please Select valid city.";
        }
        if (!error.equals("")) {
            showAlert.setMessage(Html.fromHtml("<font color='#00a1e3'>" + error + "</font>"));
            showAlert.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // TODO Auto-generated method stub

                        }
                    });
            showAlert.show();
            return false;
        }
        return true;
    }

}
