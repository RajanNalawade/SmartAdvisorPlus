package sbilife.com.pointofsale_bancaagency.new_bussiness.pivc;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonForAllProd;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;


public class NewBussinessInstaPIVCActivity extends AppCompatActivity implements View.OnClickListener, ServiceHits.DownLoadData {

    private final String NAME_SPACE = "http://tempuri.org/";
    private final String METHOD_NAME_GET_PIVC_DETAILS = "getdetail_PIVC";
    private Context mContext;
    private CommonMethods mCommonMethods;
    private EditText edtNBInstaPIVC;
    private ProgressDialog mProgressDialog;
    private String strProposalNo = "", strUIN = "", strPIVCFlag = "", strProductCode = "", strPolicyTerm = "", strPIVCDetails = "";
    private ParseXML mParseXML;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_new_bussiness_insta_pivc);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        initialise();
    }

    public void initialise() {

        mContext = this;
        mCommonMethods = new CommonMethods();
        mCommonMethods.setApplicationToolbarMenu(this, "Insta PIWC");

        mProgressDialog = new ProgressDialog(mContext,
                ProgressDialog.THEME_HOLO_LIGHT);

        mParseXML = new ParseXML();

        edtNBInstaPIVC = findViewById(R.id.edtNBInstaPIVC);

        Button btnNBInstaPIVC = findViewById(R.id.btnNBInstaPIVC);
        btnNBInstaPIVC.setOnClickListener(this);
    }

    @Override
    public void downLoadData() {
        strPIVCFlag = METHOD_NAME_GET_PIVC_DETAILS;
        new GetPIVCDetails().execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNBInstaPIVC:

                strProposalNo = edtNBInstaPIVC.getText().toString();
                strProposalNo = strProposalNo == null ? "" : strProposalNo;

                if (strProposalNo.length() == 10) {

                    String strProCode = strProposalNo.substring(0, 2);

                    if (strProCode.equals(getString(R.string.sbi_life_smart_bachat_code))) {
                        strProductCode = getString(R.string.sbi_life_smart_bachat_code);
                        strUIN = "111N108V03";
                    } else if (strProCode.equals(getString(R.string.sbi_life_smart_elite_code))) {
                        strProductCode = getString(R.string.sbi_life_smart_elite_code);
                        strUIN = "111L072V04";
                    } else if (strProCode.equals(getString(R.string.sbi_life_smart_wealth_builder_code))) {
                        strProductCode = getString(R.string.sbi_life_smart_wealth_builder_code);
                        strUIN = "111L095V03";
                    } else if (strProCode.equals(getString(R.string.sbi_life_smart_money_back_gold_code))) {
                        strProductCode = getString(R.string.sbi_life_smart_money_back_gold_code);
                        strUIN = "111N096V03";
                    } else if (strProCode.equals(getString(R.string.sbi_life_smart_swadhan_plus_code))) {
                        strProductCode = getString(R.string.sbi_life_smart_swadhan_plus_code);
                        strUIN = "111N104V02";
                    } else if (strProCode.equals(getString(R.string.sbi_life_smart_privilege_code))) {
                        strProductCode = getString(R.string.sbi_life_smart_privilege_code);
                        strUIN = "111L107V03";
                    }/*new 16 Products*/ else if (strProCode
                            .equals(getString(R.string.sbi_life_saral_insure_wealth_plus_code))) {
                        strProductCode = getString(R.string.sbi_life_saral_insure_wealth_plus_code);
                        strUIN = "111L124V01";
                    } else if (strProCode
                            .equals(getString(R.string.sbi_life_saral_maha_anand_code))) {
                        strProductCode = getString(R.string.sbi_life_saral_maha_anand_code);
                        strUIN = "111L070V02";
                    } else if (strProCode
                            .equals(getString(R.string.sbi_life_smart_insure_wealth_plus_code))) {
                        strProductCode = getString(R.string.sbi_life_smart_insure_wealth_plus_code);
                        strUIN = "111L125V01";
                    } else if (strProCode
                            .equals(getString(R.string.sbi_life_smart_power_insurance_code))) {
                        strProductCode = getString(R.string.sbi_life_smart_power_insurance_code);
                        strUIN = "111L090V02";
                    } else if (strProCode
                            .equals(getString(R.string.sbi_life_smart_wealth_assure_code))) {
                        strProductCode = getString(R.string.sbi_life_smart_wealth_assure_code);
                        strUIN = "111L077V03";
                    } else if (strProCode
                            .equals(getString(R.string.sbi_life_Retire_smart_code))) {
                        strProductCode = getString(R.string.sbi_life_Retire_smart_code);
                        strUIN = "111L094V02";
                    } else if (strProCode
                            .equals(getString(R.string.sbi_life_saral_pension_code))) {
                        strProductCode = getString(R.string.sbi_life_saral_pension_code);
                        strUIN = "111N088V03";
                    } else if (strProCode
                            .equals(getString(R.string.sbi_life_shubh_nivesh_code))) {
                        strProductCode = getString(R.string.sbi_life_shubh_nivesh_code);
                        strUIN = "111N055V04";
                    } else if (strProCode
                            .equals(getString(R.string.sbi_life_smart_income_protect_code))) {
                        strProductCode = getString(R.string.sbi_life_smart_income_protect_code);
                        strUIN = "111N085V04";
                    } else if (strProCode
                            .equals(getString(R.string.sbi_life_smart_money_planner_code))) {
                        strProductCode = getString(R.string.sbi_life_smart_money_planner_code);
                        strUIN = "111N101V03";
                    } else if (strProCode
                            .equals(getString(R.string.sbi_life_smart_women_advantage_code))) {
                        strProductCode = getString(R.string.sbi_life_smart_women_advantage_code);
                        strUIN = "111N106V01";
                    } else if (strProCode
                            .equals(getString(R.string.sbi_life_smart_scholar_code))) {
                        strProductCode = getString(R.string.sbi_life_smart_scholar_code);
                        strUIN = "111L073V03";
                    } else if (strProCode
                            .equals(getString(R.string.sbi_life_smart_champ_insurance_code))) {
                        strProductCode = getString(R.string.sbi_life_smart_champ_insurance_code);
                        strUIN = "111N098V03";
                    } else if (strProCode
                            .equals(getString(R.string.sbi_life_saral_shield_code))) {
                        strProductCode = getString(R.string.sbi_life_saral_shield_code);
                        strUIN = "111N066V03";
                    } else if (strProCode
                            .equals(getString(R.string.sbi_life_smart_shield_code))) {
                        strProductCode = getString(R.string.sbi_life_smart_shield_code);
                        strUIN = "111N067V07";
                    } else if (strProCode.equals(getString(R.string.sbi_life_flexi_smart_plus_code))) {
                        strProductCode = getString(R.string.sbi_life_flexi_smart_plus_code);
                        strUIN = "111N093V01";
                    } else if (strProCode.equals(getString(R.string.sbi_life_smart_platina_assure_code))) {
                        strProductCode = getString(R.string.sbi_life_smart_platina_assure_code);
                        strUIN = "111N126V04";
                    } else if (strProCode.equals(getString(R.string.sbi_life_sampoorn_cancer_suraksha_code))) {
                        strProductCode = getString(R.string.sbi_life_sampoorn_cancer_suraksha_code);
                        strUIN = "111N109V03";
                    } else if (strProCode.equals(getString(R.string.sbi_life_poorn_suraksha_code))) {
                        strProductCode = getString(R.string.sbi_life_poorn_suraksha_code);
                        strUIN = "111N110V03";
                    } else if (strProCode.equals(getString(R.string.sbi_life_saral_swadhan_plus_code))) {
                        strProductCode = getString(R.string.sbi_life_saral_swadhan_plus_code);
                        strUIN = "111N092V03";
                    } else if (strProCode.equals(getString(R.string.sbi_life_annuity_plus_code))) {
                        strProductCode = getString(R.string.sbi_life_annuity_plus_code);
                        strUIN = "111N083V11";
                    } else if (strProCode.equals(getString(R.string.sbi_life_smart_humsafar_code))) {
                        strProductCode = getString(R.string.sbi_life_smart_humsafar_code);
                        strUIN = "111N103V03";
                    } else if (strProCode.equals(getString(R.string.sbi_life_smart_future_choices_code))) {
                        strProductCode = getString(R.string.sbi_life_smart_future_choices_code);
                        strUIN = getString(R.string.sbi_life_smart_future_choices_uin);
                    } else if (strProCode.equals(getString(R.string.sbi_life_saral_jeevan_bima_code))) {
                        strProductCode = getString(R.string.sbi_life_saral_jeevan_bima_code);
                        strUIN = getString(R.string.sbi_life_saral_jeevan_bima_uin);
                    } else if (strProCode.equals(getString(R.string.sbi_life_saral_pension_new_code))) {
                        strProductCode = getString(R.string.sbi_life_saral_pension_new_code);
                        strUIN = getString(R.string.sbi_life_saral_pension_new_uin);
                    }else if (strProCode.equals(getString(R.string.sbi_life_eshield_next_code))) {
                        strProductCode = getString(R.string.sbi_life_eshield_next_code);
                        strUIN = getString(R.string.sbi_life_eshield_next_uin);
                    } else if (strProCode.equals(getString(R.string.sbi_life_smart_platina_plus_code))) {
                        strProductCode = getString(R.string.sbi_life_smart_platina_plus_code);
                        strUIN = getString(R.string.sbi_life_smart_platina_plus_uin);
                    } else {
                        strProductCode = "";
                        strUIN = "";
                        strProposalNo = "";
                        mCommonMethods.showToast(mContext, "Currently this process is not available for this product");
                    }

                    if (!strUIN.equals("")) {
                        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = mCommonMethods.setUserDetails(mContext);

                        ServiceHits service = new ServiceHits(mContext,
                                METHOD_NAME_GET_PIVC_DETAILS, strProposalNo, userDetailsValuesModel.getStrCIFBDMUserId(),
                                userDetailsValuesModel.getStrCIFBDMEmailId(), userDetailsValuesModel.getStrCIFBDMMObileNo(),
                                userDetailsValuesModel.getStrCIFBDMPassword(), this);
                        service.execute();
                    }

                } else {
                    mCommonMethods.showToast(mContext, "Please enter 10 digit proposal number");
                }

                break;

            default:
                break;
        }
    }

    public class GetPIVCDetails extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String strResOutput = "";

        @Override
        protected void onPreExecute() {

            if (!mProgressDialog.isShowing()) {
                mProgressDialog = new ProgressDialog(mContext,
                        ProgressDialog.THEME_HOLO_LIGHT);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.setTitle(Html.fromHtml("<font color='#00a1e3'><b>Please Wait...<b></font>"));
                mProgressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            if (mCommonMethods.isNetworkConnected(mContext)) {
                try {
                    running = true;

                    HttpConnector1.setServerCert(getResources().openRawResource(R.raw.combohttpscertificate));

                    switch (strPIVCFlag) {

                        case METHOD_NAME_GET_PIVC_DETAILS:
                            SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME_GET_PIVC_DETAILS);

                            request.addProperty("PropNo", strProposalNo);
                            request.addProperty("authkey", "SBIL");
                            request.addProperty("source", "PIVC");

                            mCommonMethods.TLSv12Enable();

                            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                            envelope.dotNet = true;

                            new MarshalBase64().register(envelope); // serialization

                            envelope.setOutputSoapObject(request);

                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                    .permitAll().build();
                            StrictMode.setThreadPolicy(policy);

                            String URL = ServiceURL.SERVICE_URL;
                            HttpTransportSE androidHttpTranport = new HttpTransportSE(URL, 50000);
                            try {
                                androidHttpTranport.call("http://tempuri.org/getdetail_PIVC", envelope);
                                Object response = envelope.getResponse();
                                strResOutput = response.toString();
                            } catch (Exception e) {
                                e.printStackTrace();
                                running = false;
                                return mCommonMethods.WEEK_INTERNET_MESSAGE;
                            }

                            break;

                        case "GETSTATUS":
                            String RequestData = "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"sbil_proposal_no\"\r\n\r\n" + strProposalNo + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"sbil_source\"\r\n\r\nSMARTADVISOR\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"sbil_uin_no\"\r\n\r\n" + strUIN + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--";
                            //strResOutput = HttpConnector1.getInstance().postData_PIVCGETLINK("https://pivc.sbilife.co.in/portal/api/kfd/getPosPIVCLinkStatus", PreProposalNumber, RequestData);

                            //or production
                            strResOutput = HttpConnector1.getInstance().postData_PIVCGETLINK("https://pivc.sbilife.co.in/portal/api/pivc/getPIVCLinkStatus", strProposalNo, RequestData);

                            //for uat
                            //strResOutput = HttpConnector1.getInstance().postData_PIVCGETLINK("https://pivc-uat.sbilife.co.in/portal/api/pivc/getPIVCLinkStatus", strProposalNo, RequestData);
                            break;

                        case "GETLINK":

                            //parse xml
                            String strParse = mParseXML.parseXmlTag(strPIVCDetails, "NewDataSet");
                            strParse = mParseXML.parseXmlTag(strParse, "Table");

                            String CUSTOMER_NAME = mParseXML.parseXmlTag(strParse, "CUSTOMER_NAME");
                            CUSTOMER_NAME = CUSTOMER_NAME == null ? "" : CUSTOMER_NAME;

                            String GENDER = mParseXML.parseXmlTag(strParse, "GENDER");
                            GENDER = GENDER == null ? "" : GENDER;

                            String MOBILE_NUMBER = mParseXML.parseXmlTag(strParse, "MOBILE_NUMBER");
                            MOBILE_NUMBER = MOBILE_NUMBER == null ? "" : MOBILE_NUMBER;

                            String DOB_PH = mParseXML.parseXmlTag(strParse, "DOB_PH");
                            DOB_PH = DOB_PH == null ? "" : DOB_PH;

                            String SUM_ASSURED = mParseXML.parseXmlTag(strParse, "SUM_ASSURED");
                            SUM_ASSURED = SUM_ASSURED == null ? "" : SUM_ASSURED;

                            String FREQUENCY = mParseXML.parseXmlTag(strParse, "FREQUENCY");
                            FREQUENCY = FREQUENCY == null ? "" : FREQUENCY;

                            String PAYMENT_TERM = mParseXML.parseXmlTag(strParse, "PAYMENT_TERM");
                            PAYMENT_TERM = PAYMENT_TERM == null ? "" : PAYMENT_TERM;

                            String BENEFIT_TERM = mParseXML.parseXmlTag(strParse, "BENEFIT_TERM");
                            BENEFIT_TERM = BENEFIT_TERM == null ? "" : BENEFIT_TERM;

                            String PRODUCT = mParseXML.parseXmlTag(strParse, "PRODUCT");
                            PRODUCT = PRODUCT == null ? "" : PRODUCT;

                            String PRODUCT_CATEGORY = mParseXML.parseXmlTag(strParse, "PRODUCT_CATEGORY");
                            PRODUCT_CATEGORY = PRODUCT_CATEGORY == null ? "" : PRODUCT_CATEGORY;

                            String NBD_PLAN_TYPE = mParseXML.parseXmlTag(strParse, "NBD_PLAN_TYPE");
                            NBD_PLAN_TYPE = NBD_PLAN_TYPE == null ? "" : NBD_PLAN_TYPE;

                            String PLAN = mParseXML.parseXmlTag(strParse, "PLAN");
                            PLAN = PLAN == null ? "" : PLAN;

                            String RIDER1_SA = mParseXML.parseXmlTag(strParse, "RIDER1_SA");
                            RIDER1_SA = RIDER1_SA == null ? "" : RIDER1_SA;

                            String RIDER1_TERM = mParseXML.parseXmlTag(strParse, "RIDER1_TERM");
                            RIDER1_TERM = RIDER1_TERM == null ? "" : RIDER1_TERM;

                            String RIDER2_SA = mParseXML.parseXmlTag(strParse, "RIDER2_SA");
                            RIDER2_SA = RIDER2_SA == null ? "" : RIDER2_SA;

                            String RIDER2_TERM = mParseXML.parseXmlTag(strParse, "RIDER2_TERM");
                            RIDER2_TERM = RIDER2_TERM == null ? "" : RIDER2_TERM;

                            String RIDER3_SA = mParseXML.parseXmlTag(strParse, "RIDER3_SA");
                            RIDER3_SA = RIDER3_SA == null ? "" : RIDER3_SA;

                            String RIDER3_TERM = mParseXML.parseXmlTag(strParse, "RIDER3_TERM");
                            RIDER3_TERM = RIDER3_TERM == null ? "" : RIDER3_TERM;

                            String RIDER4_SA = mParseXML.parseXmlTag(strParse, "RIDER4_SA");
                            RIDER4_SA = RIDER4_SA == null ? "" : RIDER4_SA;

                            String RIDER4_TERM = mParseXML.parseXmlTag(strParse, "RIDER4_TERM");
                            RIDER4_TERM = RIDER4_TERM == null ? "" : RIDER4_TERM;

                            String MAILINGADDRESS1 = mParseXML.parseXmlTag(strParse, "MAILINGADDRESS1");
                            MAILINGADDRESS1 = MAILINGADDRESS1 == null ? "" : MAILINGADDRESS1;

                            String MAILINGADDRESS2 = mParseXML.parseXmlTag(strParse, "MAILINGADDRESS2");
                            MAILINGADDRESS2 = MAILINGADDRESS2 == null ? "" : MAILINGADDRESS2;

                            String MAILINGADDRESS3 = mParseXML.parseXmlTag(strParse, "MAILINGADDRESS3");
                            MAILINGADDRESS3 = MAILINGADDRESS3 == null ? "" : MAILINGADDRESS3;

                            String MAILINGCITY = mParseXML.parseXmlTag(strParse, "MAILINGCITY");
                            MAILINGCITY = MAILINGCITY == null ? "" : MAILINGCITY;

                            String MAILINGPINCODE = mParseXML.parseXmlTag(strParse, "MAILINGPINCODE");
                            MAILINGPINCODE = MAILINGPINCODE == null ? "" : MAILINGPINCODE;

                            String MAILINGSTATE = mParseXML.parseXmlTag(strParse, "MAILINGSTATE");
                            MAILINGSTATE = MAILINGSTATE == null ? "" : MAILINGSTATE;

                            String PERMANENTADDRESS1 = mParseXML.parseXmlTag(strParse, "PERMANENTADDRESS1");
                            PERMANENTADDRESS1 = PERMANENTADDRESS1 == null ? "" : PERMANENTADDRESS1;

                            String PERMANENTADDRESS2 = mParseXML.parseXmlTag(strParse, "PERMANENTADDRESS2");
                            PERMANENTADDRESS2 = PERMANENTADDRESS2 == null ? "" : PERMANENTADDRESS2;

                            String PERMANENTADDRESS3 = mParseXML.parseXmlTag(strParse, "PERMANENTADDRESS3");
                            PERMANENTADDRESS3 = PERMANENTADDRESS3 == null ? "" : PERMANENTADDRESS3;

                            String PERMANENTCITY = mParseXML.parseXmlTag(strParse, "PERMANENTCITY");
                            PERMANENTCITY = PERMANENTCITY == null ? "" : PERMANENTCITY;

                            String PERMANENTPINCODE = mParseXML.parseXmlTag(strParse, "PERMANENTPINCODE");
                            PERMANENTPINCODE = PERMANENTPINCODE == null ? "" : PERMANENTPINCODE;

                            String PERMANENTSTATE = mParseXML.parseXmlTag(strParse, "PERMANENTSTATE");
                            PERMANENTSTATE = PERMANENTSTATE == null ? "" : PERMANENTSTATE;

                            String EMAIL = mParseXML.parseXmlTag(strParse, "EMAIL");
                            EMAIL = EMAIL == null ? "" : EMAIL;

                            String NOMINEE_NAME = mParseXML.parseXmlTag(strParse, "NOMINEE_NAME");
                            NOMINEE_NAME = NOMINEE_NAME == null ? "" : NOMINEE_NAME;

                            String NOMINEE_RELATION = mParseXML.parseXmlTag(strParse, "NOMINEE_RELATION");
                            NOMINEE_RELATION = NOMINEE_RELATION == null ? "" : NOMINEE_RELATION;

                            String PREFERED_LANG = mParseXML.parseXmlTag(strParse, "PREFERED_LANG");
                            PREFERED_LANG = PREFERED_LANG == null ? "" : PREFERED_LANG;

                            String PROPOSER_OCCUPATION = mParseXML.parseXmlTag(strParse, "PROPOSER_OCCUPATION");
                            PROPOSER_OCCUPATION = PROPOSER_OCCUPATION == null ? "" : PROPOSER_OCCUPATION;

                            String PREMIUM_AMOUNT = mParseXML.parseXmlTag(strParse, "NBD_PREMIUM_AMT");
                            PREMIUM_AMOUNT = PREMIUM_AMOUNT == null ? "" : PREMIUM_AMOUNT;

                            String DISTRIBUTOR_ID = mParseXML.parseXmlTag(strParse, "DISTRIBUTOR_ID");
                            DISTRIBUTOR_ID = DISTRIBUTOR_ID == null ? "" : DISTRIBUTOR_ID;

                            String CHANNEL_NAME = mParseXML.parseXmlTag(strParse, "CHANNELNAME");
                            CHANNEL_NAME = CHANNEL_NAME == null ? "" : CHANNEL_NAME;

                            String payoutPeriod = mParseXML.parseXmlTag(strParse, "NBD_PAYOUT_PERIOD_OPTED");
                            payoutPeriod = payoutPeriod == null ? "" : payoutPeriod;

                            String strGuaranteedPayoutFrequency = mParseXML.parseXmlTag(strParse, "NBD_GUARANTEED_INCOME_FREQ");
                            strGuaranteedPayoutFrequency = strGuaranteedPayoutFrequency == null ? "" : strGuaranteedPayoutFrequency;

                            String PAYMENT_FREQUENCY = "";
                            String RIDER_NAME = "";
                            String POLICY_MATURITY_DATE = "";
                            String SOURCE = "";
                            String CASHIERING_BRANCH = "";
                            String ZONE = "";
                            String PREMIUM_POLICY_TYPE = NBD_PLAN_TYPE;
                            String SALUTATION = "";
                            String REGION_NAME = "";

                            CommonForAllProd obj = new CommonForAllProd();
                            String strBI4 = mParseXML.parseXmlTag(strParse, "NBMD_BI_4") == null ? "" : mParseXML.parseXmlTag(strParse, "NBMD_BI_4");
                            String strBI8 = mParseXML.parseXmlTag(strParse, "NBMD_BI_8") == null ? "" : mParseXML.parseXmlTag(strParse, "NBMD_BI_8");
                            strBI4 = obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((strBI4.equals("") || strBI4 == null) ? "0" : strBI4))) + "";
                            strBI8 = obj.getRound(obj.getStringWithout_E(Double
                                    .valueOf((strBI8.equals("") || strBI8 == null) ? "0" : strBI8))) + "";

                            //for annuity PIVC strats
                            String NBMD_MATURITY_VALUE_LESS = mParseXML.parseXmlTag(strParse, "NBMD_MATURITY_VALUE_LESS");
                            NBMD_MATURITY_VALUE_LESS = NBMD_MATURITY_VALUE_LESS == null ? "" : NBMD_MATURITY_VALUE_LESS;

                            String AFA_NAME_TITLE = mParseXML.parseXmlTag(strParse, "AFA_NAME_TITLE");
                            AFA_NAME_TITLE = AFA_NAME_TITLE == null ? "" : AFA_NAME_TITLE;

                            String AFA_NAME_FIRST_NAME = mParseXML.parseXmlTag(strParse, "AFA_NAME_FIRST_NAME");
                            AFA_NAME_FIRST_NAME = AFA_NAME_FIRST_NAME == null ? "" : AFA_NAME_FIRST_NAME;

                            String AFA_NAME_MIDDLE_NAME = mParseXML.parseXmlTag(strParse, "AFA_NAME_MIDDLE_NAME");
                            AFA_NAME_MIDDLE_NAME = AFA_NAME_MIDDLE_NAME == null ? "" : AFA_NAME_MIDDLE_NAME;

                            String AFA_NAME_LAST_NAME = mParseXML.parseXmlTag(strParse, "AFA_NAME_LAST_NAME");
                            AFA_NAME_LAST_NAME = AFA_NAME_LAST_NAME == null ? "" : AFA_NAME_LAST_NAME;

                            String ASA_NAME_TITLE = mParseXML.parseXmlTag(strParse, "ASA_NAME_TITLE");
                            ASA_NAME_TITLE = ASA_NAME_TITLE == null ? "" : ASA_NAME_TITLE;

                            String ASA_NAME_FIRST_NAME = mParseXML.parseXmlTag(strParse, "ASA_NAME_FIRST_NAME");
                            ASA_NAME_FIRST_NAME = ASA_NAME_FIRST_NAME == null ? "" : ASA_NAME_FIRST_NAME;


                            String ASA_NAME_MIDDLE_NAME = mParseXML.parseXmlTag(strParse, "ASA_NAME_MIDDLE_NAME");
                            ASA_NAME_MIDDLE_NAME = ASA_NAME_MIDDLE_NAME == null ? "" : ASA_NAME_MIDDLE_NAME;

                            String ASA_NAME_LAST_NAME = mParseXML.parseXmlTag(strParse, "ASA_NAME_LAST_NAME");
                            ASA_NAME_LAST_NAME = ASA_NAME_LAST_NAME == null ? "" : ASA_NAME_LAST_NAME;

                            String annuity_amount_payable = mParseXML.parseXmlTag(strParse, "ANNUITY_AMOUNT_PAYABLE");
                            annuity_amount_payable = annuity_amount_payable == null ? "0" : annuity_amount_payable;

                            String AFA_NAME = "";
                            String ASA_NAME = "";
                            String str_Annuity_data = "";
                            String str_Humsafar_data = "";

                            if (!AFA_NAME_FIRST_NAME.equalsIgnoreCase("")) {
                                AFA_NAME = AFA_NAME_TITLE + " " + AFA_NAME_FIRST_NAME + " " + AFA_NAME_MIDDLE_NAME + " " + AFA_NAME_LAST_NAME;
                            }

                            if (!ASA_NAME_FIRST_NAME.equalsIgnoreCase("")) {
                                ASA_NAME = ASA_NAME_TITLE + " " + ASA_NAME_FIRST_NAME + " " + ASA_NAME_MIDDLE_NAME + " " + ASA_NAME_LAST_NAME;
                            }

                            String AD_NAME = mParseXML.parseXmlTag(strParse, "AD_NAME");
                            AD_NAME = AD_NAME == null ? "" : AD_NAME;


                            String AD_GENDER = mParseXML.parseXmlTag(strParse, "AD_GENDER");
                            AD_GENDER = AD_GENDER == null ? "" : AD_GENDER;

                            String AD_DOB = mParseXML.parseXmlTag(strParse, "AD_DOB");
                            AD_DOB = AD_DOB == null ? "" : AD_DOB;

                            String AD_OCCUPATION = mParseXML.parseXmlTag(strParse, "AD_OCCUPATION");
                            AD_OCCUPATION = AD_OCCUPATION == null ? "" : AD_OCCUPATION;

                            String NBM_COMBO_FLAG = mParseXML.parseXmlTag(strParse, "NBM_COMBO_FLAG");
                            NBM_COMBO_FLAG = NBM_COMBO_FLAG == null ? "" : NBM_COMBO_FLAG;


                            if (strProductCode.equals(getString(R.string.sbi_life_annuity_plus_code))) {

                                str_Annuity_data = "<AFA_NAME>" + AFA_NAME + "</AFA_NAME>" +
                                        "<ASA_NAME>" + ASA_NAME + "</ASA_NAME>"
                                        +
                                        "<ANNUITY_AMOUNT>" + annuity_amount_payable + "</ANNUITY_AMOUNT>";
                            }


                            if (strProductCode
                                    .equals(getString(R.string.sbi_life_smart_humsafar_code))) {
                                str_Humsafar_data = "<AD_NAME>" + AD_NAME + "</AD_NAME>" +
                                        "<AD_GENDER>" + AD_GENDER + "</AD_GENDER>"
                                        +
                                        "<AD_DOB>" + AD_DOB + "</AD_DOB>"
                                        +
                                        "<AD_OCCUPATION>" + AD_OCCUPATION + "</AD_OCCUPATION>";
                            }

                            String NBMD_GUARANTEED_ADDITION = mParseXML.parseXmlTag(strParse, "NBMD_GUARANTEED_ADDITION");
                            NBMD_GUARANTEED_ADDITION = NBMD_GUARANTEED_ADDITION == null ? "0" : NBMD_GUARANTEED_ADDITION;

                            String str_smart_platina_plus = "";
                            if (strProductCode
                                    .equals(getString(R.string.sbi_life_smart_platina_plus_code))) {
                                str_smart_platina_plus = "<strIncomePlan>" + PLAN + "</strIncomePlan>" +
                                        "<strGuaranteedPayoutFrequency>" + strGuaranteedPayoutFrequency + "</strGuaranteedPayoutFrequency>"
                                        +
                                        "<payoutPeriod>" + payoutPeriod + "</payoutPeriod>" +

                                        "<GuaranteedIncome>" + strBI4 + "</GuaranteedIncome>";
                            }

                            //for annuity PIVC ends

                            if (strProductCode.contains(getString(R.string.sbi_life_smart_bachat_code))) {
                                if (!RIDER1_SA.equals("")) {
                                    RIDER_NAME = "Accidental Death and Total Permanent Disability Benefit";
                                }
                            } else if (strProductCode.contains(getString(R.string.sbi_life_smart_elite_code))) {
                                if (!RIDER1_SA.equals("")) {
                                    RIDER_NAME = "Accident Benefit";
                                }
                            } else if (strProductCode.contains(getString(R.string.sbi_life_smart_wealth_builder_code))) {
                                RIDER_NAME = "";
                            } else if (strProductCode.contains(getString(R.string.sbi_life_smart_money_back_gold_code))) {

                                String str_rider_2_name = "";
                                String str_rider_1_name = "";
                                String str_rider_3_name = "";
                                String str_rider_4_name = "";

                                if (!RIDER2_SA.equals("")) {
                                    str_rider_2_name = "SBI Life - Accidental Death Benefit (ADB) Rider (UIN: 111B015V02)";
                                }
                                if (!RIDER1_SA.equals("")) {
                                    str_rider_1_name = "SBI Life - Preferred Term Rider (UIN: 111B014V02)";

                                }
                                if (!RIDER3_SA.equals("")) {
                                    str_rider_3_name = "SBI Life - Accidental Total and Permanent Disability Benefit Rider (UIN: 111B016V02)";

                                }
                                if (!RIDER4_SA.equals("")) {
                                    str_rider_4_name = "SBI Life - Criti Care 13 Non - Linked Rider (UIN: 111B025V02)";
                                }

                                if (!str_rider_2_name.equals("")) {
                                    str_rider_2_name = str_rider_2_name + ", ";
                                }
                                if (!str_rider_1_name.equals("")) {
                                    str_rider_1_name = str_rider_1_name + ", ";
                                }

                                if (!str_rider_3_name.equals("")) {
                                    str_rider_3_name = str_rider_3_name + ", ";
                                }

                                if (!str_rider_4_name.equals("")) {
                                    str_rider_4_name = str_rider_4_name + ", ";
                                }
                                RIDER_NAME = str_rider_2_name + str_rider_1_name + str_rider_3_name + str_rider_4_name;


                            } else if (strProductCode.contains(getString(R.string.sbi_life_smart_swadhan_plus_code))) {
                                RIDER_NAME = "";
                            } else if (strProductCode.contains(getString(R.string.sbi_life_smart_privilege_code))) {
                                RIDER_NAME = "";
                            } else if (strProductCode.contains(getString(R.string.sbi_life_saral_maha_anand_code))) {
                                if (!RIDER1_SA.equals("")) {
                                    RIDER_NAME = "Accidental Death Benefit Linked Rider";
                                }

                            } else if (strProductCode.contains(getString(R.string.sbi_life_smart_wealth_assure_code))) {
                                if (!RIDER1_SA.equals("")) {
                                    RIDER_NAME = "Accidental Death Benefit option";
                                }
                            } else if (strProductCode.contains(getString(R.string.sbi_life_saral_pension_code))) {
                                if (RIDER1_SA != null && !RIDER1_SA.equals("")) {
                                    RIDER_NAME = "Preferred Term Rider";
                                }
                            } else if (strProductCode.contains(getString(R.string.sbi_life_shubh_nivesh_code))) {

                                String str_rider_2_name = "";
                                String str_rider_1_name = "";
                                String str_rider_3_name = "";

                                if (!RIDER1_SA.equals("")) {
                                    str_rider_1_name = "Preferred Term Assurance Rider";
                                }
                                if (!RIDER2_SA.equals("")) {
                                    str_rider_2_name = "Accidental Death Benefit Rider";
                                }
                                if (!RIDER3_SA.equals("")) {
                                    str_rider_3_name = "Accidental Total and Permanent Disability Benefit Rider";
                                }
                                if (!str_rider_2_name.equals("")) {
                                    str_rider_2_name = str_rider_2_name + ", ";
                                }
                                if (!str_rider_1_name.equals("")) {
                                    str_rider_1_name = str_rider_1_name + ", ";
                                }

                                if (!str_rider_3_name.equals("")) {
                                    str_rider_3_name = str_rider_3_name + ", ";
                                }


                                RIDER_NAME = str_rider_2_name + str_rider_1_name + str_rider_3_name;

                            } else if (strProductCode.contains(getString(R.string.sbi_life_smart_income_protect_code))) {
                                String str_rider_1_name = "";
                                String str_rider_2_name = "";
                                String str_rider_3_name = "";
                                String str_rider_4_name = "";

                                if (!RIDER1_SA.equals("")) {
                                    str_rider_1_name = "Preferred Term Rider";
                                }
                                if (!RIDER2_SA.equals("")) {
                                    str_rider_2_name = "Accidental Death Benefit Rider";
                                }
                                if (!RIDER3_SA.equals("")) {
                                    str_rider_3_name = "Accidental Total and Permanent Disability Benefit Rider";
                                }
                                if (!RIDER4_SA.equals("")) {
                                    str_rider_4_name = "Criti Care 13 Non - Linked Rider";
                                }
                                if (!str_rider_2_name.equals("")) {
                                    str_rider_2_name = str_rider_2_name + ", ";
                                }
                                if (!str_rider_1_name.equals("")) {
                                    str_rider_1_name = str_rider_1_name + ", ";
                                }

                                if (!str_rider_3_name.equals("")) {
                                    str_rider_3_name = str_rider_3_name + ", ";
                                }
                                if (!str_rider_4_name.equals("")) {
                                    str_rider_4_name = str_rider_4_name + ", ";
                                }
                                RIDER_NAME = str_rider_2_name + str_rider_1_name + str_rider_3_name + str_rider_4_name;

                            } else if (strProductCode.contains(getString(R.string.sbi_life_smart_women_advantage_code))) {

                                String str_rider_2_name = "";
                                String str_rider_1_name = "";

                                if (!RIDER1_SA.equals("")) {
                                    str_rider_1_name = "Critical Illness Benefit";
                                }
                                if (!RIDER2_SA.equals("")) {
                                    str_rider_2_name = "APC and CA  Option";
                                }

                                if (!str_rider_2_name.equals("")) {
                                    str_rider_2_name = str_rider_2_name + ", ";
                                }
                                if (!str_rider_1_name.equals("")) {
                                    str_rider_1_name = str_rider_1_name + ", ";
                                }

                                RIDER_NAME = str_rider_2_name + str_rider_1_name;

                            } else if (strProductCode.contains(getString(R.string.sbi_life_saral_shield_code))) {

                                String str_rider_2_name = "";
                                String str_rider_1_name = "";

                                if (!RIDER1_SA.equals("")) {
                                    str_rider_1_name = "Accidental Death Benefit Rider";
                                }
                                if (!RIDER2_SA.equals("")) {
                                    str_rider_2_name = "Accidental Total and Permanent Disability Benefit Rider";

                                }

                                if (!str_rider_2_name.equals("")) {
                                    str_rider_2_name = str_rider_2_name + ", ";
                                }
                                if (!str_rider_1_name.equals("")) {
                                    str_rider_1_name = str_rider_1_name + ", ";
                                }
                                RIDER_NAME = str_rider_2_name + str_rider_1_name;

                            } else if (strProductCode.contains(getString(R.string.sbi_life_smart_shield_code))) {

                                String str_rider_2_name = "";
                                String str_rider_1_name = "";
                                String str_rider_3_name = "";

                                if (!RIDER1_SA.equals("")) {
                                    str_rider_1_name = "Accidental Death Benefit Rider";
                                }
                                if (!RIDER2_SA.equals("")) {
                                    str_rider_2_name = "Accidental Total and Permanent Disability Benefit Rider";

                                }
                                if (!RIDER3_SA.equals("")) {
                                    str_rider_3_name = "Criti Care 13 Non Linked Rider";

                                }

                                if (!str_rider_2_name.equals("")) {
                                    str_rider_2_name = str_rider_2_name + ", ";
                                }
                                if (!str_rider_1_name.equals("")) {
                                    str_rider_1_name = str_rider_1_name + ", ";
                                }
                                if (!str_rider_3_name.equals("")) {
                                    str_rider_3_name = str_rider_1_name + ", ";
                                }
                                RIDER_NAME = str_rider_1_name + str_rider_2_name + str_rider_3_name;

                            } else if (strProductCode.contains(getString(R.string.sbi_life_annuity_plus_code))) {

                                if (!RIDER1_SA.equals("") && !RIDER2_SA.equals("")) {
                                    RIDER_NAME = "Accidental Death Benefit Rider";
                                } else if (!RIDER1_SA.equals("")) {
                                    RIDER_NAME = "Accidental Death Benefit Rider";
                                }
                            } else if (strProductCode.contains("1W")) {

                                if (RIDER1_SA != null && !RIDER1_SA.equals("")) {
                                    RIDER_NAME = "Accidental Death Benefit Rider";
                                } else if (RIDER2_SA != null && !RIDER2_SA.equals("")) {
                                    RIDER_NAME = "Accidental Death Benefit Rider";
                                }
                            }

                            if (!CUSTOMER_NAME.equals("")) {
                                String[] ProposerName_array = CUSTOMER_NAME.split(" ");
                                SALUTATION = ProposerName_array[0];
                            }

                            String sbil_data = "<NewDataSet><Table><APP_SOURCE>SMARTADVISOR</APP_SOURCE>  <PROPOSAL_NUMBER>" + strProposalNo + "</PROPOSAL_NUMBER> <GENDER>" + GENDER + "</GENDER> <CUSTOMER_NAME>" + CUSTOMER_NAME + "</CUSTOMER_NAME>" + "<MOBILE_NUMBER>" + MOBILE_NUMBER + "</MOBILE_NUMBER>" + "<DOB_PH>" + DOB_PH + "</DOB_PH>" + "<SUM_ASSURED>" + SUM_ASSURED + "</SUM_ASSURED> <FREQUENCY>" + FREQUENCY + "</FREQUENCY> <PAYMENT_TERM>" + PAYMENT_TERM + "</PAYMENT_TERM> <BENEFIT_TERM>" + BENEFIT_TERM + "</BENEFIT_TERM> <PRODUCT>" + PRODUCT + "</PRODUCT> <PRODUCT_CATEGORY>" + PRODUCT_CATEGORY + "</PRODUCT_CATEGORY> <PLAN>" + PLAN + "</PLAN>" +
                                    "<RIDER2_SA>" + RIDER2_SA + "</RIDER2_SA>" +

                                    "<RIDER2_TERM>" + RIDER2_TERM + "</RIDER2_TERM>" +


                                    "<MAILINGADDRESS1>" + MAILINGADDRESS1 + "</MAILINGADDRESS1>" +


                                    "<MAILINGADDRESS2>" + MAILINGADDRESS2 + "</MAILINGADDRESS2>" +

                                    "<MAILINGADDRESS3>" + MAILINGADDRESS3 + "</MAILINGADDRESS3>" +

                                    "<MAILINGCITY>" + MAILINGCITY + "</MAILINGCITY>" +
                                    "<MAILINGPINCODE>" + MAILINGPINCODE + "</MAILINGPINCODE>" +

                                    "<MAILINGSTATE>" + MAILINGSTATE + "</MAILINGSTATE>" +


                                    "<PERMANENTADDRESS1>" + PERMANENTADDRESS1 + "</PERMANENTADDRESS1>" +


                                    "<PERMANENTADDRESS2>" + PERMANENTADDRESS2 + "</PERMANENTADDRESS2>" +

                                    "<PERMANENTADDRESS3>" + PERMANENTADDRESS3 + "</PERMANENTADDRESS3>" +

                                    "<PERMANENTCITY>" + PERMANENTCITY + "</PERMANENTCITY>" +
                                    "<PERMANENTPINCODE>" + PERMANENTPINCODE + "</PERMANENTPINCODE>" +

                                    "<PERMANENTSTATE>" + PERMANENTSTATE + "</PERMANENTSTATE>" +


                                    "<EMAIL>" + EMAIL + "</EMAIL>" +

                                    "<NOMINEE_NAME>" + NOMINEE_NAME + "</NOMINEE_NAME>" +

                                    "<NOMINEE_RELATION>" + NOMINEE_RELATION + "</NOMINEE_RELATION>" +

                                    "<PREFERED_LANG>" + PREFERED_LANG + "</PREFERED_LANG>" +


                                    "<PROPOSER_OCCUPATION>" + PROPOSER_OCCUPATION + "</PROPOSER_OCCUPATION>" +
                                    "<SALUTATION>" + SALUTATION + "</SALUTATION>"
                                    + "<UIN_NO>" + strUIN + "</UIN_NO>" +

                                    "<PREMIUM_AMOUNT>" + PREMIUM_AMOUNT + "</PREMIUM_AMOUNT>" +


                                    "<PAYMENT_FREQUENCY>" + PAYMENT_FREQUENCY + "</PAYMENT_FREQUENCY>" +

                                    "<RIDER_NAME>" + RIDER_NAME + "</RIDER_NAME>" +
                                    "<POLICY_MATURITY_DATE>" + POLICY_MATURITY_DATE + "</POLICY_MATURITY_DATE>" +
                                    //"<BI_4>" + strBI4 + "</BI_4>" +
                                    "<BI_4>" +(strProductCode.contains(getString(R.string.sbi_life_smart_platina_plus_code))?NBMD_GUARANTEED_ADDITION : strBI4)+ "</BI_4>" +
                                    "<BI_8>" + strBI8 + "</BI_8>" +
                                    "<DISTRIBUTOR_ID>" + DISTRIBUTOR_ID + "</DISTRIBUTOR_ID>" +

                                    "<CHANNEL_NAME>" + CHANNEL_NAME + "</CHANNEL_NAME>" +

                                    "<REGION_NAME>" + REGION_NAME + "</REGION_NAME>" +

                                    "<SOURCE>" + SOURCE + "</SOURCE>" +
                                    "<CASHIERING_BRANCH>" + CASHIERING_BRANCH + "</CASHIERING_BRANCH>" +


                                    "<ZONE>" + ZONE + "</ZONE>" +

                                    "<PREMIUM_POLICY_TYPE>" + PREMIUM_POLICY_TYPE + "</PREMIUM_POLICY_TYPE>" +

                                    str_Annuity_data + str_Humsafar_data + str_smart_platina_plus +

                                    "</Table> </NewDataSet>";

                            String RequestLinkData = "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"sbil_proposal_no\"\r\n\r\n" + strProposalNo + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"sbil_source\"\r\n\r\nSMARTADVISOR\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition:form-data; name=\"sbil_uin_no\"\r\n\r\n" + strUIN + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"sbil_data\"\r\n\r\n" + sbil_data + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--";

                            if (!NBM_COMBO_FLAG.equalsIgnoreCase("Y")) {

                                //for production
                                strResOutput = HttpConnector1.getInstance().postData_PIVCGETLINK("https://pivc.sbilife.co.in/portal/api/pivc/getProposalPIVCLink", strProposalNo, RequestLinkData);

                                //for uat
                                //strResOutput = HttpConnector1.getInstance().postData_PIVCGETLINK("https://pivc-uat.sbilife.co.in/portal/api/pivc/getProposalPIVCLink", strProposalNo, RequestLinkData);

                            } else {
                                mCommonMethods.showToast(mContext, "This proposal is not applicable for this product");
                            }

                            break;

                        default:
                            break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    running = false;
                    return mCommonMethods.WEEK_INTERNET_MESSAGE;
                }
            } else {
                return mCommonMethods.NO_INTERNET_MESSAGE;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {

                if (strPIVCFlag.equals(METHOD_NAME_GET_PIVC_DETAILS)) {
                    if (!strResOutput.equals("0")) {
                        running = false;
                        strPIVCDetails = strResOutput;

                        //parse xml
                        String strParse = mParseXML.parseXmlTag(strResOutput, "NewDataSet");
                        strParse = mParseXML.parseXmlTag(strParse, "Table");

                        String combo_flag = mParseXML.parseXmlTag(strParse, "NBM_COMBO_FLAG");
                        combo_flag = combo_flag == null ? "" : combo_flag;

                        String retire_smart_flag = mParseXML.parseXmlTag(strParse, "NBD_RETIRE_SMART_PIVC");
                        retire_smart_flag = retire_smart_flag == null ? "" : retire_smart_flag;

                        if (combo_flag.equalsIgnoreCase("Y") || retire_smart_flag.equalsIgnoreCase("N")) {
                            mCommonMethods.showToast(mContext, "This proposal is not applicable for Insta-PIWC");
                        }else{
                            strPIVCFlag = "GETSTATUS";
                            new GetPIVCDetails().execute();
                        }

                    } else {
                        mCommonMethods.showToast(mContext, "This Proposal Number Dont have any details.");
                    }
                } else {

                    try {
                        if (!strResOutput.equals("")) {
                            JSONObject json_main = new JSONObject(strResOutput);
                            String PIVCLink_status = json_main.get("status").toString();
                            String PIVC_msg = json_main.get("msg").toString();

                            if (PIVCLink_status.equalsIgnoreCase("true")) {
                                String PIVC_output = json_main.get("output").toString();

                                JSONObject json_output = new JSONObject(PIVC_output);

                                if (strPIVCFlag.equalsIgnoreCase("GETSTATUS")) {
                                    String completed_status_text = json_output.get("completed_status_text").toString();
                                    boolean completed_status = (Boolean) json_output.get("completed_status");

                                    if (!completed_status
                                            || completed_status_text.equalsIgnoreCase("Not Completed")) {

                                        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                                        alert.setTitle("PIVC Process : " + strProposalNo);
                                        alert.setMessage("For PIVC Process you will navigate to Device Browser ,Kindly Complete the PIVC Process.");
                                        alert.setCancelable(false);

                                        alert.setNeutralButton("OK",
                                                new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialog,
                                                                        int id) {

                                                        //mCommonMethods.showToast(mContext, "Kindly do the PIVC Process before Payment");
                                                        dialog.dismiss();
                                                        strPIVCFlag = "GETLINK";
                                                        new GetPIVCDetails().execute();
                                                    }
                                                });
                                        alert.show();
                                    } else if (completed_status) {
                                        strProposalNo = "";
                                        mCommonMethods.showToast(mContext, "PIVC Done SuccessFully !!");
                                    }

                                } else if (strPIVCFlag.equalsIgnoreCase("GETLINK")) {
                                    PIVC_output = json_main.get("output").toString();
                                    String short_url = json_output.get("short_url").toString();
                                /*Intent myWebLink = new Intent(Intent.ACTION_VIEW);
                                myWebLink.setData(Uri.parse(short_url));
                                startActivity(myWebLink);*/
                                    mCommonMethods.openWebLink(mContext, short_url);

                                    //clear all flags strBI4 = strBI8 =
                                    strPolicyTerm = "";
                                }

                                mCommonMethods.showToast(mContext, PIVC_msg);

                            } else if (PIVC_msg.contains("Invalid proposal number")) {

                                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);

                                alert.setTitle("PIVC Process : " + strProposalNo);
                                alert.setMessage("For PIVC Process you will navigate to Device Browser ,Kindly Complete the PIVC Process.");
                                alert.setCancelable(false);

                                alert.setNeutralButton("OK",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                                //mCommonMethods.showToast(mContext, "Kindly do the PIVC Process before Payment");
                                                dialog.dismiss();

                                                strPIVCFlag = "GETLINK";
                                                new GetPIVCDetails().execute();
                                            }
                                        });
                                alert.show();
                            } else {
                                mCommonMethods.showToast(mContext, PIVC_msg);
                            }
                        } else {
                            mCommonMethods.showMessageDialog(mContext, "Link is not generated.");
                        }
                    } catch (Exception e) {
                        mCommonMethods.showToast(mContext, e.getMessage());
                    }
                }
            } else {
                mCommonMethods.showToast(mContext, s);
            }
        }
    }
}

