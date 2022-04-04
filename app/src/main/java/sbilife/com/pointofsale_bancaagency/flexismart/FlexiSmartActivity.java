package sbilife.com.pointofsale_bancaagency.flexismart;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DecimalFormat;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;


public class FlexiSmartActivity extends AppCompatActivity {

    //UI elements
    private CheckBox cb_StaffDisc;
    private CheckBox cb_BankAssuranceDisc;
    private CheckBox selHoliday;
    private CheckBox selTopup;
    private TextView txtHolidayYear;
    private TextView txtHolidayTerm;
    private TextView txtTopupPremium;
    private EditText basicSA;
    private EditText SAMF;
    private EditText HolidayYear;
    private EditText HolidayTerm;
    private EditText TopupPremium;
    private Spinner ageInYears;
    private Spinner selGender;
    private Spinner selBasicTerm;
    private Spinner selPremFreq;
    private String effectivePremium = "";
    private AlertDialog.Builder showAlert;
    private DecimalFormat currencyFormat;
    ImageButton btnMenu;

    private Context context;
    private CommonMethods mCommonMethods;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.flexismartmain);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        context = this;
        //UI elements
        cb_StaffDisc = findViewById(R.id.cb_staffdisc);
        cb_BankAssuranceDisc = findViewById(R.id.cb_BankAssuranceDisc);
        mCommonMethods = new CommonMethods();

        //Age
        ageInYears = findViewById(R.id.age);
        String[] ageList = new String[53];
        for (int i = 8; i <= 60; i++) {
            ageList[i - 8] = i + "";
        }
        ArrayAdapter<String> ageAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, ageList);
        //ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageInYears.setAdapter(ageAdapter);
        ageAdapter.notifyDataSetChanged();


        //Gender
        selGender = findViewById(R.id.selGender);
        String[] genderList = {"Male", "Female"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, genderList);
        //genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selGender.setAdapter(genderAdapter);
        genderAdapter.notifyDataSetChanged();


        //Policy Term
        selBasicTerm = findViewById(R.id.policyterm);
        String[] policyTermList = new String[11];
        for (int i = 10; i <= 20; i++) {
            policyTermList[i - 10] = i + "";
        }
        ArrayAdapter<String> policyTermAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, policyTermList);
        //policyTermAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selBasicTerm.setAdapter(policyTermAdapter);
        policyTermAdapter.notifyDataSetChanged();

        //Premium Frequency
        selPremFreq = findViewById(R.id.premiumfreq);
        String[] premFreqList = {"Yearly", "Half Yearly", "Quarterly", "Monthly"};
        ArrayAdapter<String> premFreqAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, premFreqList);
        //premFreqAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selPremFreq.setAdapter(premFreqAdapter);
        premFreqAdapter.notifyDataSetChanged();


        basicSA = findViewById(R.id.premium_amt);
        SAMF = findViewById(R.id.samf);

        selHoliday = findViewById(R.id.holiday);
        txtHolidayYear = findViewById(R.id.txtholiday_year);
        HolidayYear = findViewById(R.id.holiday_year);
        txtHolidayTerm = findViewById(R.id.txtholiday_term);
        HolidayTerm = findViewById(R.id.holiday_term);

        selTopup = findViewById(R.id.topup);
        txtTopupPremium = findViewById(R.id.txttopup_premium);
        TopupPremium = findViewById(R.id.topup_premium);

        Button back = findViewById(R.id.back);
        Button btnSubmit = findViewById(R.id.btnSubmit);

        showAlert = new AlertDialog.Builder(this);
        currencyFormat = new DecimalFormat("##,##,##,###");

        //Premium Holidays option should not be visible
        selHoliday.setVisibility(View.GONE);

        // Item Listener starts from here

        //Staff Discount
        cb_StaffDisc.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if ((cb_StaffDisc.isChecked()) && cb_BankAssuranceDisc.isChecked()) {
                    cb_StaffDisc.setChecked(true);
                    cb_BankAssuranceDisc.setChecked(false);
                }
            }
        });

        //Banca Assurance Discount
        cb_BankAssuranceDisc.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (cb_StaffDisc.isChecked() && cb_BankAssuranceDisc.isChecked()) {
                    cb_BankAssuranceDisc.setChecked(true);
                    cb_StaffDisc.setChecked(false);
                    //alertDialog.show();
                }
            }
        });


        //Holioday Year
        selHoliday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtHolidayYear.setVisibility(View.VISIBLE);
                    HolidayYear.setVisibility(View.VISIBLE);
                    txtHolidayTerm.setVisibility(View.VISIBLE);
                    HolidayTerm.setVisibility(View.VISIBLE);
                } else {
                    txtHolidayYear.setVisibility(View.GONE);
                    HolidayYear.setVisibility(View.GONE);
                    txtHolidayTerm.setVisibility(View.GONE);
                    HolidayTerm.setVisibility(View.GONE);
                }
            }
        });


        //Topup
        selTopup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtTopupPremium.setVisibility(View.VISIBLE);
                    TopupPremium.setVisibility(View.VISIBLE);
                } else {
                    txtTopupPremium.setVisibility(View.GONE);
                    TopupPremium.setVisibility(View.GONE);
                }
            }
        });


        //Go Home Button
        back.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                setResult(RESULT_OK);
                finish();
            }
        });


        //Submit Button
        btnSubmit.setOnClickListener(new OnClickListener() {


            //@Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                //calculate effective premium
                if (!basicSA.getText().toString().equals(""))
                    setEffectivePremium();

                if (vallidate())
                    addListenerOnSubmit();
            }
        });

        //Age
        ageInYears.setOnItemSelectedListener(new OnItemSelectedListener() {

            //@Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long id) {
                valMaturityAge();
            }

            //@Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        //Basic Term
        selBasicTerm.setOnItemSelectedListener(new OnItemSelectedListener() {

            //@Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long id) {
                valMaturityAge();
            }

            //@Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        // Item Listener ends here
    }

    //Store user input in bean object
    private void addListenerOnSubmit() {

        String NAMESPACE = "http://tempuri.org/";
        String URL = ServiceURL.SERVICE_URL;
        String SOAP_ACTION = "http://tempuri.org/getPremiumFlexiSmart";
        String METHOD_NAME = "getPremiumFlexiSmart";


        //    Service starts here

        ProgressDialog progressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Please Wait";
        progressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle(Html
                .fromHtml("<font color='#00a1e3'><b>Calculating<b></font>"));
        progressDialog.setMax(100);
        progressDialog.show();
        AsyncFlexiSmart service = new AsyncFlexiSmart(this, progressDialog, NAMESPACE, URL, SOAP_ACTION, METHOD_NAME);
        service.execute();
        // Service ends here


    }


    /************************* Validation starts from here ******************************************/


    //Calculate effective premium
    private void setEffectivePremium() {
        try {
            int divFactor = 0;

            if (selPremFreq.getSelectedItem().toString().equals("Monthly")) {
                divFactor = 50;
            } else if (selPremFreq.getSelectedItem().toString().equals("Quarterly")) {
                divFactor = 50;
            } else if (selPremFreq.getSelectedItem().toString().equals("Half Yearly")) {
                divFactor = 100;
            } else if (selPremFreq.getSelectedItem().toString().equals("Yearly")) {
                divFactor = 100;
            }

            if ((Integer.parseInt(basicSA.getText().toString()) % divFactor) == 0) {
                effectivePremium = basicSA.getText().toString();
                ////System.out.println("Effective Premium  : "+(Integer.parseInt(premiumAmt.getString())));
            } else {
                effectivePremium = (Integer.parseInt(basicSA.getText().toString()) - (Integer.parseInt(basicSA.getText().toString()) % divFactor)) + "";
                ////System.out.println("Effective Premium  : "+(Integer.parseInt(premiumAmt.getString())-(Integer.parseInt(premiumAmt.getString())  % divFactor)));
            }
        } catch (Exception e) {/**/}
    }

    //validate premium amount, SAMF, holiday year , holiday term, topup premium
    //Validation after Submit Button is pressed

    private boolean vallidate() {

        StringBuilder error = new StringBuilder();
        //Premium Amount validation
        double minPremAmt = 0;
        if (selPremFreq.getSelectedItem().toString().equals("Monthly")) {
            minPremAmt = 1500;
        } else if (selPremFreq.getSelectedItem().toString().equals("Quarterly")) {
            minPremAmt = 4500;
        } else if (selPremFreq.getSelectedItem().toString().equals("Half Yearly")) {
            minPremAmt = 8500;
        } else if (selPremFreq.getSelectedItem().toString().equals("Yearly")) {
            minPremAmt = 15000;
        }

        if (basicSA.getText().toString().equals("") || Double.parseDouble(basicSA.getText().toString()) < minPremAmt) {
            error.append("Premium Amount should not be less than Rs. ").append(minPremAmt);

            //topup with initial premium

        } else if ((selPremFreq.getSelectedItem().toString().equals("Yearly") || selPremFreq.getSelectedItem().toString().equals("Half Yearly")) && ((Double.parseDouble(basicSA.getText().toString())) % 100 != 0)) {
            error.append("Premium Amount should be multiple of 100");
        } else if ((selPremFreq.getSelectedItem().toString().equals("Monthly") || selPremFreq.getSelectedItem().toString().equals("Quarterly")) && ((Double.parseDouble(basicSA.getText().toString())) % 50 != 0)) {
            error.append("Premium Amount should be multiple of 50");
        } else {
            if (selTopup.isChecked()) {
                double minTopup = 2000;
                double maxTopup = Double.parseDouble(effectivePremium);

                if (TopupPremium.getText().toString().equals("") || Double.parseDouble(TopupPremium.getText().toString()) < minTopup || Double.parseDouble(TopupPremium.getText().toString()) > maxTopup) {
                    error.append("\nEnter Topup Premium Amount between ").append(currencyFormat.format(minTopup)).append(" and ").append(currencyFormat.format(maxTopup));
                } else if ((Double.parseDouble(TopupPremium.getText().toString())) % 100 != 0) {
                    error.append("\nTopup Premium Amount should be multiple of 100");
                }
            }
        }

        //SAMF validation
        double minSAMF = 10;
        double maxSAMF = 20;

        if (SAMF.getText().toString().equals("") || Double.parseDouble(SAMF.getText().toString()) < minSAMF || Double.parseDouble(SAMF.getText().toString()) > maxSAMF) {
            error.append("\nEnter Sum Assured Multiple Factor [SAMF] between ").append(minSAMF).append(" and ").append(maxSAMF);
        }

        //Premium Holiday
        if (selHoliday.isChecked()) {
            //policy year
            double minHolidayYear = 6;
            double maxHolidayYear = Double.parseDouble(selBasicTerm.getSelectedItem().toString());
            if (HolidayYear.getText().toString().equals("") || Double.parseDouble(HolidayYear.getText().toString()) < minHolidayYear || Double.parseDouble(HolidayYear.getText().toString()) > maxHolidayYear) {
                error.append("\nEnter Policy Year for Holiday between ").append(minHolidayYear).append(" and ").append(maxHolidayYear).append(" years");
            }
            //premium term
            int minHolidayTerm = 1;
            int maxHolidayTerm = 3;
            if (HolidayTerm.getText().toString().equals("") || Double.parseDouble(HolidayTerm.getText().toString()) < minHolidayTerm || Double.parseDouble(HolidayTerm.getText().toString()) > maxHolidayTerm) {
                error.append("\nEnter Premium Term for Holiday between ").append(minHolidayTerm).append(" and ").append(maxHolidayTerm).append(" years");
            }

        }
        if (!error.toString().equals("")) {

            showAlert.setMessage(error.toString());
            showAlert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                //@Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            showAlert.show();
            return false;
        } else
            return true;
    }


    //maturity age of policy is 70 years
    private void valMaturityAge() {
        int Age = Integer.parseInt(ageInYears.getSelectedItem().toString());
        int PolicyTerm = Integer.parseInt(selBasicTerm.getSelectedItem().toString());
        if ((Age + PolicyTerm) > 70) {
            showAlert.setMessage("Maturity age is 70 years");
            showAlert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                //@Override
                public void onClick(DialogInterface dialog, int which) {
                    selBasicTerm.setSelection(0);
                }
            });
            showAlert.show();
        }

    }

    //Validation ends here


    /****************************** Async Task starts here *******************************************/


    class AsyncFlexiSmart extends AsyncTask<String, Void, String> {
        final Context mContext;
        ProgressDialog progressDialog = null;
        String NAMESPACE = "";
        String URL = "";
        String SOAP_ACTION = "";
        String METHOD_NAME = "";
        String sumAssured, MatBenefit6, MatBenefit10;
        final DecimalFormat currencyFormat;
        String ageInYears_text, selGender_text, selBasicTerm_text, selPremFreq_text, basicSA_text, SAMF_text, HolidayYear_text,
                HolidayTerm_text, TopupPremium_text;
        Boolean cb_StaffDisc_check, cb_BankAssuranceDisc_check, selHoliday_check, selTopup_check;

        AsyncFlexiSmart(Context mContext, ProgressDialog progressDialog, String NAMESPACE, String URL, String SOAP_ACTION, String METHOD_NAME) {
            // TODO Auto-generated constructor stub

            this.NAMESPACE = NAMESPACE;
            this.URL = URL;
            this.SOAP_ACTION = SOAP_ACTION;
            this.METHOD_NAME = METHOD_NAME;
            this.mContext = mContext;
            this.progressDialog = progressDialog;
            currencyFormat = new DecimalFormat("##,##,##,###");
        }

        @Override
        protected void onPreExecute() {
            cb_StaffDisc_check = cb_StaffDisc.isChecked();
            cb_BankAssuranceDisc_check = cb_BankAssuranceDisc.isChecked();
            ageInYears_text = ageInYears.getSelectedItem().toString();
            selGender_text = selGender.getSelectedItem().toString();
            selBasicTerm_text = selBasicTerm.getSelectedItem().toString();
            selPremFreq_text = selPremFreq.getSelectedItem().toString();
            basicSA_text = basicSA.getText().toString();
            SAMF_text = SAMF.getText().toString();
            selHoliday_check = selHoliday.isChecked();
            HolidayYear_text = HolidayYear.getText().toString();
            HolidayTerm_text = HolidayTerm.getText().toString();
            selTopup_check = selTopup.isChecked();
            TopupPremium_text = TopupPremium.getText().toString();

        }

        @Override
        protected String doInBackground(String... param) {
            // TODO Auto-generated method stub

            if (mCommonMethods.isNetworkConnected(context)) {

                try {
                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                    request.addProperty("isStaff", String.valueOf(cb_StaffDisc_check));
                    request.addProperty("isBancAssuranceDisc", String.valueOf(cb_BankAssuranceDisc_check));
                    request.addProperty("age", ageInYears_text);
                    request.addProperty("gender", selGender_text);
                    request.addProperty("policyTerm", selBasicTerm_text);
                    request.addProperty("premFreq", selPremFreq_text);
                    request.addProperty("effectivePremium", effectivePremium);
                    request.addProperty("premiumAmount", basicSA_text);
                    request.addProperty("SAMF", SAMF_text);

                    if (selHoliday_check) {
                        request.addProperty("isHoliday", "Yes");
                        request.addProperty("holidayYear", HolidayYear_text);
                        request.addProperty("holidayTerm", HolidayTerm_text);
                    } else {
                        request.addProperty("isHoliday", "No");
                        request.addProperty("holidayYear", "0");
                        request.addProperty("holidayTerm", "0");
                    }

                    if (selTopup_check) {
                        request.addProperty("isTopup", "true");
                        request.addProperty("topupPremAmt", TopupPremium_text);
                    } else {
                        request.addProperty("isTopup", "false");
                        request.addProperty("topupPremAmt", "0");
                    }


                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    //Enable this envelope if service is written in dot net
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

                    //allowAllSSL();
                    mCommonMethods.TLSv12Enable();

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    androidHttpTransport.call(SOAP_ACTION, envelope);
                    SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

                    String result = response.toString();

//					System.out.println("result " + result);

                    if (!result.contains("<errorMessage>")) {
                        ParseXML prsObj = new ParseXML();
                        result = prsObj.parseXmlTag(result, "flexiSmartInsurance");
                        sumAssured = prsObj.parseXmlTag(result, "sumAssured");
                        MatBenefit6 = prsObj.parseXmlTag(result, "MatBenefit6");
                        MatBenefit10 = prsObj.parseXmlTag(result, "MatBenefit10");
                        return "Success";

                    } else {
                        return "Server not Found. Please try after some time.";
                    }

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

            if (progressDialog.isShowing())
                progressDialog.dismiss();

            if (result.equals("Success")) {

                Intent i = new Intent(mContext, success.class);

                // Input to display starts from here
                i.putExtra("ip1", "Age : " + ageInYears.getSelectedItem().toString());
                i.putExtra("ip2", "Policy Term : " + selBasicTerm.getSelectedItem().toString());
                i.putExtra("ip3", "Premium Payment Mode : " + selPremFreq.getSelectedItem().toString());
                i.putExtra("ip4", "Premium Amount(Rs.) : " + basicSA.getText().toString());
                i.putExtra("ip5", "Sum Assured Multiple Factor[SAMF] : " + SAMF.getText().toString());

                // Input to display ends here ******************************************/

                i.putExtra("op", "Sum Assured is Rs." + currencyFormat.format(Double.parseDouble(sumAssured)));
                i.putExtra("op1", "Maturity Benefit Payable @ 6% is Rs." + currencyFormat.format(Double.parseDouble(MatBenefit6)));
                i.putExtra("op2", "Maturity Benefit Payable @ 10% is Rs." + currencyFormat.format(Double.parseDouble(MatBenefit10)));
                mContext.startActivity(i);


            } else {
                Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public boolean dispatchTouchEvent(final MotionEvent ev) {
        // all touch events close the keyboard before they are processed except EditText instances.
        // if focus is an EditText we need to check, if the touchevent was inside the focus editTexts
        final View currentFocus = getCurrentFocus();
        if (!(currentFocus instanceof EditText) || !isTouchInsideView(ev, currentFocus)) {
            ((InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isTouchInsideView(final MotionEvent ev, final View currentFocus) {
        final int[] loc = new int[2];
        currentFocus.getLocationOnScreen(loc);
        return ev.getRawX() > loc[0] && ev.getRawY() > loc[1] && ev.getRawX() < (loc[0] + currentFocus.getWidth())
                && ev.getRawY() < (loc[1] + currentFocus.getHeight());
    }


}
