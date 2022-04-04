package sbilife.com.pointofsale_bancaagency.home;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.util.Date;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;

public class ScoreCardActivity extends AppCompatActivity implements ServiceHits.DownLoadData {
    private final String METHOD_NAME_SCORE_CARD = "geScorecard";

    private final String URl = ServiceURL.SERVICE_URL;
    private ProgressDialog mProgressDialog;

    private DownloadScoreCardAsync downloadScoreCardAsync;

    private Context context;
    private CommonMethods commonMethods;
    private StorageUtils mStorageUtils;

    private String strCIFBDMUserId, strCIFBDMMObileNo, strCIFBDMPassword, strCIFBDMEmailId, userType;
    private ServiceHits service;

    private File scoreCardFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_score_card);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        context = this;
        commonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();

        commonMethods.setApplicationToolbarMenu(this, "Score Card");

        userType = commonMethods.GetUserType(context);
        mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading Please wait...";
        mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);

        mProgressDialog.setButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (downloadScoreCardAsync != null) {
                            downloadScoreCardAsync.cancel(true);
                        }
                        if (mProgressDialog != null) {
                            if (mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                        }
                    }
                });

        mProgressDialog.setMax(100);

        Intent intent = getIntent();
        String fromHome = intent.getStringExtra("fromHome");
        if (fromHome != null && fromHome.equalsIgnoreCase("N")) {
            strCIFBDMUserId = intent.getStringExtra("strBDMCifCOde");
            strCIFBDMEmailId = intent.getStringExtra("strEmailId");
            strCIFBDMMObileNo = intent.getStringExtra("strMobileNo");
            userType = intent.getStringExtra("strUserType");
            try {
                strCIFBDMPassword = SimpleCrypto.encrypt("SBIL", "sbil");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods
                    .setUserDetails(context);
            strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
            strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
            strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
            strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
        }

        service_hits();
    }

    @Override
    public void downLoadData() {
        downloadScoreCardAsync = new DownloadScoreCardAsync();
        downloadScoreCardAsync.execute();
    }

    class DownloadScoreCardAsync extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String strRevivalListErrorCOde1 = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                String NAMESPACE = "http://tempuri.org/";
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_SCORE_CARD);
                request.addProperty("strCode", strCIFBDMUserId);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                String SOAP_ACTION_RENEWAL_LIST_UPDATE = NAMESPACE + METHOD_NAME_SCORE_CARD;
                androidHttpTranport.call(SOAP_ACTION_RENEWAL_LIST_UPDATE,
                        envelope);

                SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();
                String inputpolicylist = sa.toString();

                if (!inputpolicylist.equalsIgnoreCase("0")) {
                    ParseXML prsObj = new ParseXML();

                    inputpolicylist = prsObj.parseXmlTag(
                            inputpolicylist, "ppcDS");

                    strRevivalListErrorCOde1 = prsObj.parseXmlTag(
                            inputpolicylist, "ScreenData");

                    if (strRevivalListErrorCOde1 == null) {

                        String resultNode = prsObj.parseXmlTag(inputpolicylist, "Table");

                        createScoreCardPDF(prsObj, resultNode);

                        strRevivalListErrorCOde1 = "success";
                    } else {
                        strRevivalListErrorCOde1 = "0";
                    }
                } else {
                    strRevivalListErrorCOde1 = "0";
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


            if (running) {
                if (strRevivalListErrorCOde1 != null && strRevivalListErrorCOde1.equalsIgnoreCase("success")) {

                    commonMethods.showPDFFile(context, scoreCardFile);
                } else {
                    commonMethods.showMessageDialog(context, "Currently Data is not available try after sometime.");
                }
            } else {
                commonMethods.showMessageDialog(context, "Currently Data is not available try after sometime.");
            }
        }
    }

    private void createScoreCardPDF(ParseXML prsObj, String resultNode) {

                   /* <ppcDS>
                <Table>
                    <CODE>11030562</CODE>
                    <NAME>PANKAJ GUPTA</NAME>
                    <HEADING>Operations Quality Score Card-BSM</HEADING>
                    <FIELD_1>BSM CODE</FIELD_1>
                    <FIELD_2>BSM NAME</FIELD_2>
                    <NO_OF_IA>143</NO_OF_IA>
                    <CDA_AMOUNT>.25</CDA_AMOUNT>
                    <REGION>JAIPUR</REGION>
                    <DOJ>22-MAY-2006</DOJ>
                    <TOTAL_NB>2.58</TOTAL_NB>
                    <TOTAL_RENEWALS>2.66</TOTAL_RENEWALS>

                    <NB_REFUND>.04</NB_REFUND>
                    <TOTAL_SURRENDER>.23</TOTAL_SURRENDER>
                    <FTR_ACHV>57.35</FTR_ACHV>
                    <FTR_SCORE>20.00</FTR_SCORE>
                    <AUTO_UNDERWRITING_ACHV>.00</AUTO_UNDERWRITING_ACHV>
                    <AUTO_UNDERWRITING_SCORE>5</AUTO_UNDERWRITING_SCORE>
                    <NB_REFUND_ACHV>1.54</NB_REFUND_ACHV>
                    <NB_REFUND_SCORE>20</NB_REFUND_SCORE>
                    <PIWC_MISMATCH_ACHV>10.00</PIWC_MISMATCH_ACHV>
                    <PIWC_MISMATCH_SCORE>25.00</PIWC_MISMATCH_SCORE>
                    <ONLINE_PAYMENT_ACHV>45.81</ONLINE_PAYMENT_ACHV>
                    <ONLINE_PAYMENT_SCORE>0</ONLINE_PAYMENT_SCORE>
                    <DIGITAL_ADOPT_ACHV>75.52</DIGITAL_ADOPT_ACHV>
                    <DIGITAL_ADOPT_SCORE>5</DIGITAL_ADOPT_SCORE>


                    <EARLY_DEATH_CLAIM_ACHV>1.00</EARLY_DEATH_CLAIM_ACHV>
                    <EARLY_DEATH_CLAIM_SCORE>25</EARLY_DEATH_CLAIM_SCORE>
                    <MIS_SELL_ACHV>.00</MIS_SELL_ACHV>
                    <MIS_SELL_SCORE>25.00</MIS_SELL_SCORE>
                    <PERSISTENCY_13>86.00</PERSISTENCY_13>
                    <PERSISTENCY_13_SCORE>20</PERSISTENCY_13_SCORE>
                    <ALT_MODE_REG_ACHV>67.31</ALT_MODE_REG_ACHV>
                    <ALT_MODE_REG_SCORE>15</ALT_MODE_REG_SCORE>
                    <TOTAL_SCORE>280</TOTAL_SCORE>
                    <REGIONAL_RANK>5</REGIONAL_RANK>
                    <PAN_INDIA_RANK>147</PAN_INDIA_RANK>
                    <MONTH>31-JAN-19</MONTH>
                    <NOP>629</NOP>
                    <TENURE>12.70</TENURE>


                    <PERSISTENCY_37>.00</PERSISTENCY_37>
                    <PERSISTENCY_37_SCORE>0</PERSISTENCY_37_SCORE>
                    <DEATH_CLAIM_AMOUNT>.00</DEATH_CLAIM_AMOUNT>
                    <NB_NON_MED_TAT_ACHV>.00</NB_NON_MED_TAT_ACHV>
                    <NB_NON_MED_TAT_SCORE>5</NB_NON_MED_TAT_SCORE>
                    <SMART_ADV_ADOP_ACHV>36.92</SMART_ADV_ADOP_ACHV>
                    <SMART_ADV_ADOP_SCORE>0</SMART_ADV_ADOP_SCORE>
                    <ESHIKSHA_USAGE_ACHV>1.49</ESHIKSHA_USAGE_ACHV>
                    <ESHIKSHA_USAGE_SCORE>0</ESHIKSHA_USAGE_SCORE>
                    <CUSTOMER_SELFSRV_ACHV>4.29</CUSTOMER_SELFSRV_ACHV>
                    <CUSTOMER_SELFSRV_SCORE>5</CUSTOMER_SELFSRV_SCORE>
                    <FLC_ISSUENCE_ACHV>.32</FLC_ISSUENCE_ACHV>
                    <FLC_ISSUENCE_SCORE>20</FLC_ISSUENCE_SCORE>
                    <YTD_RENEWAL_ACHV>107.82</YTD_RENEWAL_ACHV>
                    <YTD_RENEWAL_SCORE>25</YTD_RENEWAL_SCORE>
                    <NPS_ACHV>50</NPS_ACHV>

                    <NPS_SCORE>25</NPS_SCORE>
                    <REQUIREMENT_PENDING_ACHV>11.29</REQUIREMENT_PENDING_ACHV>
                    <REQUIREMENT_PENDING_SCORE>25</REQUIREMENT_PENDING_SCORE>
                    <PERSISTENCY_61_SCORE>0</PERSISTENCY_61_SCORE>
                    <PERSISTENCY_61>41.89</PERSISTENCY_61>
                    <PNR_COUNT_SCORE>25</PNR_COUNT_SCORE>
                    <PIWC_EFFICIENCY_ACHV>87.15</PIWC_EFFICIENCY_ACHV>
                    <PIWC_EFFICIENCY_SCORE>0</PIWC_EFFICIENCY_SCORE>
                    <UPLOAD_MONTH>JAN-19</UPLOAD_MONTH>
                </Table>
            </ppcDS>*/
//Manish
        BaseColor titlePDFTableColor = WebColors.getRGBColor("#006699");
        BaseColor userCodeTitleCellColor = WebColors.getRGBColor("#BED0E2");
        BaseColor userCodeCellColor = WebColors.getRGBColor("#C3EEEE");
        BaseColor NOPCellColor = WebColors.getRGBColor("#B4ABAB");
        BaseColor digParamCellOneColor = WebColors.getRGBColor("#CCCCCC");
        BaseColor digParamTableRowColor = WebColors.getRGBColor("#FFCC99");
        BaseColor busQualityParamTableRowColor = WebColors.getRGBColor("#E5B0B0");
        BaseColor NBParameterTableRowColor = WebColors.getRGBColor("#BCD4D4");
        BaseColor PSParameterTableRowColor = WebColors.getRGBColor("#BDBD89");
        BaseColor DocumentBackgroundColor = WebColors.getRGBColor("#CCFFFF");
        BaseColor YellowHeaderFontColor = WebColors.getRGBColor("#FFFF66");

        String CODE, NAME, HEADING, FIELD_1, FIELD_2, NO_OF_IA, CDA_AMOUNT, REGION, DOJ, TOTAL_NB, TOTAL_RENEWALS, NB_REFUND,
                TOTAL_SURRENDER, FTR_ACHV, FTR_SCORE, AUTO_UNDERWRITING_ACHV, AUTO_UNDERWRITING_SCORE, NB_REFUND_ACHV,
                NB_REFUND_SCORE, PIWC_MISMATCH_ACHV, PIWC_MISMATCH_SCORE, ONLINE_PAYMENT_ACHV, ONLINE_PAYMENT_SCORE,
                DIGITAL_ADOPT_ACHV, DIGITAL_ADOPT_SCORE, EARLY_DEATH_CLAIM_ACHV, EARLY_DEATH_CLAIM_SCORE, MIS_SELL_ACHV,
                MIS_SELL_SCORE, PERSISTENCY_13, PERSISTENCY_13_SCORE, ALT_MODE_REG_ACHV, ALT_MODE_REG_SCORE, TOTAL_SCORE,
                REGIONAL_RANK, PAN_INDIA_RANK, MONTH, NOP, TENURE, PERSISTENCY_37, PERSISTENCY_37_SCORE, DEATH_CLAIM_AMOUNT,
                NB_NON_MED_TAT_ACHV, NB_NON_MED_TAT_SCORE, SMART_ADV_ADOP_ACHV, SMART_ADV_ADOP_SCORE, ESHIKSHA_USAGE_ACHV,
                ESHIKSHA_USAGE_SCORE, CUSTOMER_SELFSRV_ACHV, CUSTOMER_SELFSRV_SCORE, FLC_ISSUENCE_ACHV, FLC_ISSUENCE_SCORE,
                YTD_RENEWAL_ACHV, YTD_RENEWAL_SCORE, NPS_ACHV, NPS_SCORE, REQUIREMENT_PENDING_ACHV, REQUIREMENT_PENDING_SCORE,
                PERSISTENCY_61_SCORE, PERSISTENCY_61, PNR_COUNT_SCORE, PIWC_EFFICIENCY_ACHV, PIWC_EFFICIENCY_SCORE, UPLOAD_MONTH, PNR_COUNT_ACHV,
                GROUP_REFUND_ACHV, GROUP_REFUND_SCORE;

        CODE = prsObj.parseXmlTag(resultNode, "CODE");
        CODE = CODE == null ? "0" : CODE;

        NAME = prsObj.parseXmlTag(resultNode, "NAME");
        NAME = NAME == null ? "" : NAME;

        HEADING = prsObj.parseXmlTag(resultNode, "HEADING");
        HEADING = HEADING == null ? "" : HEADING;

        FIELD_1 = prsObj.parseXmlTag(resultNode, "FIELD_1");
        FIELD_1 = FIELD_1 == null ? "" : FIELD_1;

        FIELD_2 = prsObj.parseXmlTag(resultNode, "FIELD_2");
        FIELD_2 = FIELD_2 == null ? "" : FIELD_2;

        NO_OF_IA = prsObj.parseXmlTag(resultNode, "NO_OF_IA");
        NO_OF_IA = NO_OF_IA == null ? "" : NO_OF_IA;

        CDA_AMOUNT = prsObj.parseXmlTag(resultNode, "CDA_AMOUNT");
        CDA_AMOUNT = CDA_AMOUNT == null ? "0.0" : CDA_AMOUNT;

        REGION = prsObj.parseXmlTag(resultNode, "REGION");
        REGION = REGION == null ? "" : REGION;

        DOJ = prsObj.parseXmlTag(resultNode, "DOJ");
        DOJ = DOJ == null ? "" : DOJ;

        TOTAL_NB = prsObj.parseXmlTag(resultNode, "TOTAL_NB");
        TOTAL_NB = TOTAL_NB == null ? "0.0" : TOTAL_NB;

        TOTAL_RENEWALS = prsObj.parseXmlTag(resultNode, "TOTAL_RENEWALS");
        TOTAL_RENEWALS = TOTAL_RENEWALS == null ? "0.0" : TOTAL_RENEWALS;

        NB_REFUND = prsObj.parseXmlTag(resultNode, "NB_REFUND");
        NB_REFUND = NB_REFUND == null ? "0.0" : NB_REFUND;

        TOTAL_SURRENDER = prsObj.parseXmlTag(resultNode, "TOTAL_SURRENDER");
        TOTAL_SURRENDER = TOTAL_SURRENDER == null ? "0.0" : TOTAL_SURRENDER;

        FTR_ACHV = prsObj.parseXmlTag(resultNode, "FTR_ACHV");
        FTR_ACHV = FTR_ACHV == null ? "0.0" : FTR_ACHV;

        FTR_SCORE = prsObj.parseXmlTag(resultNode, "FTR_SCORE");
        FTR_SCORE = FTR_SCORE == null ? "0.0" : FTR_SCORE;

        AUTO_UNDERWRITING_ACHV = prsObj.parseXmlTag(resultNode, "AUTO_UNDERWRITING_ACHV");
        AUTO_UNDERWRITING_ACHV = AUTO_UNDERWRITING_ACHV == null ? "0.0" : AUTO_UNDERWRITING_ACHV;

        AUTO_UNDERWRITING_SCORE = prsObj.parseXmlTag(resultNode, "AUTO_UNDERWRITING_SCORE");
        AUTO_UNDERWRITING_SCORE = AUTO_UNDERWRITING_SCORE == null ? "0" : AUTO_UNDERWRITING_SCORE;

        NB_REFUND_ACHV = prsObj.parseXmlTag(resultNode, "NB_REFUND_ACHV");
        NB_REFUND_ACHV = NB_REFUND_ACHV == null ? "0.0" : NB_REFUND_ACHV;

        NB_REFUND_SCORE = prsObj.parseXmlTag(resultNode, "NB_REFUND_SCORE");
        NB_REFUND_SCORE = NB_REFUND_SCORE == null ? "0" : NB_REFUND_SCORE;

        PIWC_MISMATCH_ACHV = prsObj.parseXmlTag(resultNode, "PIWC_MISMATCH_ACHV");
        PIWC_MISMATCH_ACHV = PIWC_MISMATCH_ACHV == null ? "0.0" : PIWC_MISMATCH_ACHV;

        PIWC_MISMATCH_SCORE = prsObj.parseXmlTag(resultNode, "PIWC_MISMATCH_SCORE");
        PIWC_MISMATCH_SCORE = PIWC_MISMATCH_SCORE == null ? "0" : PIWC_MISMATCH_SCORE;

        ONLINE_PAYMENT_ACHV = prsObj.parseXmlTag(resultNode, "ONLINE_PAYMENT_ACHV");
        ONLINE_PAYMENT_ACHV = ONLINE_PAYMENT_ACHV == null ? "0.0" : ONLINE_PAYMENT_ACHV;

        ONLINE_PAYMENT_SCORE = prsObj.parseXmlTag(resultNode, "ONLINE_PAYMENT_SCORE");
        ONLINE_PAYMENT_SCORE = ONLINE_PAYMENT_SCORE == null ? "0" : ONLINE_PAYMENT_SCORE;

        DIGITAL_ADOPT_ACHV = prsObj.parseXmlTag(resultNode, "DIGITAL_ADOPT_ACHV");
        DIGITAL_ADOPT_ACHV = DIGITAL_ADOPT_ACHV == null ? "0.0" : DIGITAL_ADOPT_ACHV;

        DIGITAL_ADOPT_SCORE = prsObj.parseXmlTag(resultNode, "DIGITAL_ADOPT_SCORE");
        DIGITAL_ADOPT_SCORE = DIGITAL_ADOPT_SCORE == null ? "0" : DIGITAL_ADOPT_SCORE;

        EARLY_DEATH_CLAIM_ACHV = prsObj.parseXmlTag(resultNode, "EARLY_DEATH_CLAIM_ACHV");
        EARLY_DEATH_CLAIM_ACHV = EARLY_DEATH_CLAIM_ACHV == null ? "0.0" : EARLY_DEATH_CLAIM_ACHV;

        EARLY_DEATH_CLAIM_SCORE = prsObj.parseXmlTag(resultNode, "EARLY_DEATH_CLAIM_SCORE");
        EARLY_DEATH_CLAIM_SCORE = EARLY_DEATH_CLAIM_SCORE == null ? "0" : EARLY_DEATH_CLAIM_SCORE;

        MIS_SELL_ACHV = prsObj.parseXmlTag(resultNode, "MIS_SELL_ACHV");
        MIS_SELL_ACHV = MIS_SELL_ACHV == null ? "0.0" : MIS_SELL_ACHV;

        MIS_SELL_SCORE = prsObj.parseXmlTag(resultNode, "MIS_SELL_SCORE");
        MIS_SELL_SCORE = MIS_SELL_SCORE == null ? "0.0" : MIS_SELL_SCORE;

        PERSISTENCY_13 = prsObj.parseXmlTag(resultNode, "PERSISTENCY_13");
        PERSISTENCY_13 = PERSISTENCY_13 == null ? "0.0" : PERSISTENCY_13;

        PERSISTENCY_13_SCORE = prsObj.parseXmlTag(resultNode, "PERSISTENCY_13_SCORE");
        PERSISTENCY_13_SCORE = PERSISTENCY_13_SCORE == null ? "0" : PERSISTENCY_13_SCORE;

        ALT_MODE_REG_ACHV = prsObj.parseXmlTag(resultNode, "ALT_MODE_REG_ACHV");
        ALT_MODE_REG_ACHV = ALT_MODE_REG_ACHV == null ? "0.0" : ALT_MODE_REG_ACHV;

        ALT_MODE_REG_SCORE = prsObj.parseXmlTag(resultNode, "ALT_MODE_REG_SCORE");
        ALT_MODE_REG_SCORE = ALT_MODE_REG_SCORE == null ? "0" : ALT_MODE_REG_SCORE;

        TOTAL_SCORE = prsObj.parseXmlTag(resultNode, "TOTAL_SCORE");
        TOTAL_SCORE = TOTAL_SCORE == null ? "0" : TOTAL_SCORE;

        REGIONAL_RANK = prsObj.parseXmlTag(resultNode, "REGIONAL_RANK");
        REGIONAL_RANK = REGIONAL_RANK == null ? "0" : REGIONAL_RANK;

        PAN_INDIA_RANK = prsObj.parseXmlTag(resultNode, "PAN_INDIA_RANK");
        PAN_INDIA_RANK = PAN_INDIA_RANK == null ? "0" : PAN_INDIA_RANK;

        MONTH = prsObj.parseXmlTag(resultNode, "MONTH");
        MONTH = MONTH == null ? "" : MONTH;

        NOP = prsObj.parseXmlTag(resultNode, "NOP");
        NOP = NOP == null ? "0" : NOP;

        TENURE = prsObj.parseXmlTag(resultNode, "TENURE");
        TENURE = TENURE == null ? "0.0" : TENURE;

        PERSISTENCY_37 = prsObj.parseXmlTag(resultNode, "PERSISTENCY_37");
        PERSISTENCY_37 = PERSISTENCY_37 == null ? "0.0" : PERSISTENCY_37;

        PERSISTENCY_37_SCORE = prsObj.parseXmlTag(resultNode, "PERSISTENCY_37_SCORE");
        PERSISTENCY_37_SCORE = PERSISTENCY_37_SCORE == null ? "0" : PERSISTENCY_37_SCORE;

        DEATH_CLAIM_AMOUNT = prsObj.parseXmlTag(resultNode, "DEATH_CLAIM_AMOUNT");
        DEATH_CLAIM_AMOUNT = DEATH_CLAIM_AMOUNT == null ? "0.0" : DEATH_CLAIM_AMOUNT;

        NB_NON_MED_TAT_ACHV = prsObj.parseXmlTag(resultNode, "NB_NON_MED_TAT_ACHV");
        NB_NON_MED_TAT_ACHV = NB_NON_MED_TAT_ACHV == null ? "0.0" : NB_NON_MED_TAT_ACHV;

        NB_NON_MED_TAT_SCORE = prsObj.parseXmlTag(resultNode, "NB_NON_MED_TAT_SCORE");
        NB_NON_MED_TAT_SCORE = NB_NON_MED_TAT_SCORE == null ? "0" : NB_NON_MED_TAT_SCORE;

        SMART_ADV_ADOP_ACHV = prsObj.parseXmlTag(resultNode, "SMART_ADV_ADOP_ACHV");
        SMART_ADV_ADOP_ACHV = SMART_ADV_ADOP_ACHV == null ? "0.0" : SMART_ADV_ADOP_ACHV;

        SMART_ADV_ADOP_SCORE = prsObj.parseXmlTag(resultNode, "SMART_ADV_ADOP_SCORE");
        SMART_ADV_ADOP_SCORE = SMART_ADV_ADOP_SCORE == null ? "0" : SMART_ADV_ADOP_SCORE;

        ESHIKSHA_USAGE_ACHV = prsObj.parseXmlTag(resultNode, "ESHIKSHA_USAGE_ACHV");
        ESHIKSHA_USAGE_ACHV = ESHIKSHA_USAGE_ACHV == null ? "0.0" : ESHIKSHA_USAGE_ACHV;

        ESHIKSHA_USAGE_SCORE = prsObj.parseXmlTag(resultNode, "ESHIKSHA_USAGE_SCORE");
        ESHIKSHA_USAGE_SCORE = ESHIKSHA_USAGE_SCORE == null ? "0" : ESHIKSHA_USAGE_SCORE;

        CUSTOMER_SELFSRV_ACHV = prsObj.parseXmlTag(resultNode, "CUSTOMER_SELFSRV_ACHV");
        CUSTOMER_SELFSRV_ACHV = CUSTOMER_SELFSRV_ACHV == null ? "0.0" : CUSTOMER_SELFSRV_ACHV;

        CUSTOMER_SELFSRV_SCORE = prsObj.parseXmlTag(resultNode, "CUSTOMER_SELFSRV_SCORE");
        CUSTOMER_SELFSRV_SCORE = CUSTOMER_SELFSRV_SCORE == null ? "0" : CUSTOMER_SELFSRV_SCORE;

        FLC_ISSUENCE_ACHV = prsObj.parseXmlTag(resultNode, "FLC_ISSUENCE_ACHV");
        FLC_ISSUENCE_ACHV = FLC_ISSUENCE_ACHV == null ? "0.0" : FLC_ISSUENCE_ACHV;

        FLC_ISSUENCE_SCORE = prsObj.parseXmlTag(resultNode, "FLC_ISSUENCE_SCORE");
        FLC_ISSUENCE_SCORE = FLC_ISSUENCE_SCORE == null ? "0" : FLC_ISSUENCE_SCORE;

        YTD_RENEWAL_ACHV = prsObj.parseXmlTag(resultNode, "YTD_RENEWAL_ACHV");
        YTD_RENEWAL_ACHV = YTD_RENEWAL_ACHV == null ? "0.0" : YTD_RENEWAL_ACHV;

        YTD_RENEWAL_SCORE = prsObj.parseXmlTag(resultNode, "YTD_RENEWAL_SCORE");
        YTD_RENEWAL_SCORE = YTD_RENEWAL_SCORE == null ? "0" : YTD_RENEWAL_SCORE;

        NPS_ACHV = prsObj.parseXmlTag(resultNode, "NPS_ACHV");
        NPS_ACHV = NPS_ACHV == null ? "0" : NPS_ACHV;

        NPS_SCORE = prsObj.parseXmlTag(resultNode, "NPS_SCORE");
        NPS_SCORE = NPS_SCORE == null ? "0" : NPS_SCORE;

        REQUIREMENT_PENDING_ACHV = prsObj.parseXmlTag(resultNode, "REQUIREMENT_PENDING_ACHV");
        REQUIREMENT_PENDING_ACHV = REQUIREMENT_PENDING_ACHV == null ? "0.0" : REQUIREMENT_PENDING_ACHV;

        REQUIREMENT_PENDING_SCORE = prsObj.parseXmlTag(resultNode, "REQUIREMENT_PENDING_SCORE");
        REQUIREMENT_PENDING_SCORE = REQUIREMENT_PENDING_SCORE == null ? "0" : REQUIREMENT_PENDING_SCORE;

        PERSISTENCY_61_SCORE = prsObj.parseXmlTag(resultNode, "PERSISTENCY_61_SCORE");
        PERSISTENCY_61_SCORE = PERSISTENCY_61_SCORE == null ? "0" : PERSISTENCY_61_SCORE;

        PERSISTENCY_61 = prsObj.parseXmlTag(resultNode, "PERSISTENCY_61");
        PERSISTENCY_61 = PERSISTENCY_61 == null ? "0.0" : PERSISTENCY_61;

        PNR_COUNT_SCORE = prsObj.parseXmlTag(resultNode, "PNR_COUNT_SCORE");
        PNR_COUNT_SCORE = PNR_COUNT_SCORE == null ? "0" : PNR_COUNT_SCORE;

        PIWC_EFFICIENCY_ACHV = prsObj.parseXmlTag(resultNode, "PIWC_EFFICIENCY_ACHV");
        PIWC_EFFICIENCY_ACHV = PIWC_EFFICIENCY_ACHV == null ? "0.0" : PIWC_EFFICIENCY_ACHV;

        PIWC_EFFICIENCY_SCORE = prsObj.parseXmlTag(resultNode, "PIWC_EFFICIENCY_SCORE");
        PIWC_EFFICIENCY_SCORE = PIWC_EFFICIENCY_SCORE == null ? "0" : PIWC_EFFICIENCY_SCORE;

        UPLOAD_MONTH = prsObj.parseXmlTag(resultNode, "UPLOAD_MONTH");
        UPLOAD_MONTH = UPLOAD_MONTH == null ? "" : UPLOAD_MONTH;

        PNR_COUNT_ACHV = prsObj.parseXmlTag(resultNode, "PNR_COUNT_ACHV");
        PNR_COUNT_ACHV = PNR_COUNT_ACHV == null ? "0.0" : PNR_COUNT_ACHV;

        GROUP_REFUND_ACHV = prsObj.parseXmlTag(resultNode, "GROUP_REFUND_ACHV");
        GROUP_REFUND_ACHV = GROUP_REFUND_ACHV == null ? "0.0" : GROUP_REFUND_ACHV;

        GROUP_REFUND_SCORE = prsObj.parseXmlTag(resultNode, "GROUP_REFUND_SCORE");
        GROUP_REFUND_SCORE = GROUP_REFUND_SCORE == null ? "0" : GROUP_REFUND_SCORE;

        try {
            Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);

            Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 14,
                    Font.BOLD, BaseColor.WHITE);
            Font yellowHeaderBold = new Font(Font.FontFamily.TIMES_ROMAN, 11,
                    Font.BOLD, YellowHeaderFontColor);    //Manish
            Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 10,
                    Font.BOLD);

            Font small_bold_title_name = new Font(Font.FontFamily.TIMES_ROMAN, 10,
                    Font.NORMAL);

            Font small_bold1 = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.BOLD);

            String strFileName = "ScoreCard_" + strCIFBDMUserId + ".pdf";

            scoreCardFile = mStorageUtils.createFileToAppSpecificDir(context, strFileName);

            if (scoreCardFile.exists()) {
                scoreCardFile.delete();
            }


            Rectangle pageSize = new Rectangle(PageSize.A2);  //Manish
            pageSize.setBackgroundColor(DocumentBackgroundColor);  //Manish
//            Document document = new Document(PageSize.A2, 20, 20, 20, 20);
            Document document = new Document(pageSize);
            PdfWriter pdf_writer = null;
            pdf_writer = PdfWriter.getInstance(document, new FileOutputStream(scoreCardFile.getAbsolutePath()));
            document.open();

            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

            PdfPTable titlePDFTable = new PdfPTable(3);
            titlePDFTable.setWidthPercentage(100);
            PdfPCell titleTableCell = new PdfPCell(new Paragraph("Execution Date : " + currentDateTimeString, headerBold));
            titleTableCell.setBackgroundColor(titlePDFTableColor);
            titleTableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            titleTableCell.setPadding(5);
            titleTableCell.setBorder(Rectangle.NO_BORDER);
            titlePDFTable.addCell(titleTableCell);
            document.add(titlePDFTable);

            titleTableCell = new PdfPCell(new Paragraph(HEADING + " (YTD)", headerBold));
            titleTableCell.setBackgroundColor(titlePDFTableColor);
            titleTableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            titleTableCell.setPadding(5);
            titleTableCell.setBorder(Rectangle.NO_BORDER);
            titlePDFTable.addCell(titleTableCell);
            document.add(titlePDFTable);

            titleTableCell = new PdfPCell(new Paragraph(UPLOAD_MONTH, headerBold));
            titleTableCell.setBackgroundColor(titlePDFTableColor);
            titleTableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            titleTableCell.setPadding(5);
            titleTableCell.setBorder(Rectangle.NO_BORDER);
            titlePDFTable.addCell(titleTableCell);

            document.add(titlePDFTable);


            //Start Main ROW One
            //Add User Code
            PdfPTable userDetailsRowTable = new PdfPTable(2);
            userDetailsRowTable.setWidths(new float[]{9f, 9f});
            userDetailsRowTable.setWidthPercentage(100);

            PdfPCell userCodeTitleCell = new PdfPCell(new Paragraph(FIELD_1, small_bold));
            userCodeTitleCell.setBackgroundColor(userCodeTitleCellColor);
            userCodeTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            userCodeTitleCell.setPadding(5);
            userDetailsRowTable.addCell(userCodeTitleCell);

            PdfPCell userCodeCell = new PdfPCell(new Paragraph(CODE, small_bold_title_name));
            userCodeCell.setBackgroundColor(userCodeCellColor);
            userCodeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            userCodeCell.setPadding(5);
            userDetailsRowTable.addCell(userCodeCell);

            PdfPCell Nocell = new PdfPCell(new Phrase(" ", small_normal));
            Nocell.setColspan(3);
            Nocell.setBorder(Rectangle.NO_BORDER);

            //Region details
            PdfPTable regionPDFTable = new PdfPTable(2);
            regionPDFTable.setWidths(new float[]{9f, 9f});
            regionPDFTable.setSpacingBefore(3f);
            regionPDFTable.setWidthPercentage(100);

            PdfPCell regionCell = new PdfPCell(new Paragraph("Region", small_bold));
            regionCell.setBackgroundColor(userCodeTitleCellColor);
            regionCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            regionCell.setPadding(5);
            regionPDFTable.addCell(regionCell);

            PdfPCell regionNameCell = new PdfPCell(new Paragraph(REGION, small_bold_title_name));
            regionNameCell.setBackgroundColor(userCodeCellColor);
            regionNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            regionNameCell.setPadding(5);
            regionPDFTable.addCell(regionNameCell);

            //NOP Details
            PdfPTable NOPPDFTable = new PdfPTable(2);
            NOPPDFTable.setWidths(new float[]{9f, 9f});
            NOPPDFTable.setSpacingBefore(3f);
            NOPPDFTable.setWidthPercentage(100);

            PdfPCell NOPCell = new PdfPCell(new Paragraph("NOP", small_bold));
            NOPCell.setBackgroundColor(NOPCellColor);
            NOPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            NOPCell.setPadding(5);
            NOPPDFTable.addCell(NOPCell);

            PdfPCell NOPNameCell = new PdfPCell(new Paragraph(NOP, small_bold_title_name));
            NOPNameCell.setBackgroundColor(userCodeCellColor);
            NOPNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            NOPNameCell.setPadding(5);
            NOPPDFTable.addCell(NOPNameCell);


            //NB Refund Details
            PdfPTable NBRefundPDFTable = new PdfPTable(2);
            NBRefundPDFTable.setWidths(new float[]{9f, 9f});
            NBRefundPDFTable.setSpacingBefore(3f);
            NBRefundPDFTable.setWidthPercentage(100);

            PdfPCell NBRefundCell;
            PdfPCell NBRefundNameCell;

            String tableThreePageOneMainCellThree = "";
            int rowCountRSM = 0;


            if (userType.equalsIgnoreCase("UM")
                    || userType.equalsIgnoreCase("BSM")
                    || userType.equalsIgnoreCase("DSM")
                    || userType.equalsIgnoreCase("ASM")
                    || userType.equalsIgnoreCase("RSM")) {

                NBRefundCell = new PdfPCell(new Paragraph("CDA (in Cr.)", small_bold));
                NBRefundCell.setBackgroundColor(NOPCellColor);
                NBRefundCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                NBRefundCell.setPadding(5);
                NBRefundPDFTable.addCell(NBRefundCell);

                NBRefundNameCell = new PdfPCell(new Paragraph(CDA_AMOUNT + " Cr.", small_bold_title_name));
                NBRefundNameCell.setBackgroundColor(userCodeCellColor);
                NBRefundNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                NBRefundNameCell.setPadding(5);
                NBRefundPDFTable.addCell(NBRefundNameCell);

                rowCountRSM = 3;
                tableThreePageOneMainCellThree = "Online payment % for digital (Weightage 5)";

            } else {

                NBRefundCell = new PdfPCell(new Paragraph("NB Refund (in Cr.)", small_bold));
                NBRefundCell.setBackgroundColor(NOPCellColor);
                NBRefundCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                NBRefundCell.setPadding(5);
                NBRefundPDFTable.addCell(NBRefundCell);

                NBRefundNameCell = new PdfPCell(new Paragraph(NB_REFUND + " Cr.", small_bold_title_name));
                NBRefundNameCell.setBackgroundColor(userCodeCellColor);
                NBRefundNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                NBRefundNameCell.setPadding(5);
                NBRefundPDFTable.addCell(NBRefundNameCell);

                rowCountRSM = 2;

                tableThreePageOneMainCellThree = "Rinraksha Refund % (Weightage 5)";
            }

            //Out Flow
            PdfPTable outFlowPDFTable = new PdfPTable(2);
            outFlowPDFTable.setSpacingBefore(3f);
            outFlowPDFTable.setWidths(new float[]{9f, 9f});
            outFlowPDFTable.setWidthPercentage(100);

            PdfPCell outFlowCell = new PdfPCell(new Paragraph("Out Flow (in Cr.)", small_bold));
            outFlowCell.setBackgroundColor(NOPCellColor);
            outFlowCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            outFlowCell.setPadding(5);
            outFlowPDFTable.addCell(outFlowCell);

            double outFlow = Double.valueOf(DEATH_CLAIM_AMOUNT) + Double.valueOf(TOTAL_SURRENDER);

            PdfPCell outFlowNameCell = new PdfPCell(new Paragraph(outFlow + " Cr.", small_bold_title_name));
            outFlowNameCell.setBackgroundColor(userCodeCellColor);
            outFlowNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            outFlowNameCell.setPadding(5);
            outFlowPDFTable.addCell(outFlowNameCell);

            //Main Table
            PdfPTable userDetailMasterPDFTable = new PdfPTable(5);
            userDetailMasterPDFTable.setSpacingBefore(3f);
            userDetailMasterPDFTable.setWidthPercentage(100);

            PdfPCell masterHeaderCell;

            masterHeaderCell = new PdfPCell(userDetailsRowTable);
            masterHeaderCell.setPadding(5);
            masterHeaderCell.setBorder(Rectangle.NO_BORDER);
            masterHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            userDetailMasterPDFTable.addCell(masterHeaderCell);

            masterHeaderCell = new PdfPCell(regionPDFTable);
            masterHeaderCell.setPadding(5);
            masterHeaderCell.setBorder(Rectangle.NO_BORDER);
            masterHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            userDetailMasterPDFTable.addCell(masterHeaderCell);

            masterHeaderCell = new PdfPCell(NOPPDFTable);
            masterHeaderCell.setPadding(5);
            masterHeaderCell.setBorder(Rectangle.NO_BORDER);
            masterHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            userDetailMasterPDFTable.addCell(masterHeaderCell);

            masterHeaderCell = new PdfPCell(NBRefundPDFTable);
            masterHeaderCell.setPadding(5);
            masterHeaderCell.setBorder(Rectangle.NO_BORDER);
            masterHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            userDetailMasterPDFTable.addCell(masterHeaderCell);

            masterHeaderCell = new PdfPCell(outFlowPDFTable);
            masterHeaderCell.setPadding(5);
            masterHeaderCell.setBorder(Rectangle.NO_BORDER);
            masterHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            userDetailMasterPDFTable.addCell(masterHeaderCell);

            document.add(userDetailMasterPDFTable);
            //End Main ROW One

            //Start Main ROW Two


            //DOJ details
            PdfPTable DOJPDFTable = new PdfPTable(2);
            DOJPDFTable.setSpacingBefore(3f);
            DOJPDFTable.setWidths(new float[]{9f, 9f});
            DOJPDFTable.setWidthPercentage(100);

            PdfPCell DOJCell = new PdfPCell(new Paragraph("DOJ", small_bold));
            DOJCell.setBackgroundColor(userCodeTitleCellColor);
            DOJCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            DOJCell.setPadding(5);
            DOJPDFTable.addCell(DOJCell);

            PdfPCell DOJTitleCell = new PdfPCell(new Paragraph(DOJ, small_bold_title_name));
            DOJTitleCell.setBackgroundColor(userCodeCellColor);
            DOJTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            DOJTitleCell.setPadding(5);
            DOJPDFTable.addCell(DOJTitleCell);

            //Total New Business (in Cr.) details
            PdfPTable totalNBPDFTable = new PdfPTable(2);
            totalNBPDFTable.setSpacingBefore(3f);
            totalNBPDFTable.setWidths(new float[]{9f, 9f});
            totalNBPDFTable.setWidthPercentage(100);

            PdfPCell totalNBCell = new PdfPCell(new Paragraph("Total New Business (in Cr.)", small_bold));
            totalNBCell.setBackgroundColor(NOPCellColor);
            totalNBCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalNBCell.setPadding(5);
            totalNBPDFTable.addCell(totalNBCell);

            PdfPCell totalNBNameCell = new PdfPCell(new Paragraph(TOTAL_NB + " Cr.", small_bold_title_name));
            totalNBNameCell.setBackgroundColor(userCodeCellColor);
            totalNBNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalNBNameCell.setPadding(5);
            totalNBPDFTable.addCell(totalNBNameCell);

            //Main Table
            PdfPTable secondRowMasterPDFTable = new PdfPTable(rowCountRSM);
            secondRowMasterPDFTable.setSpacingBefore(3f);
            secondRowMasterPDFTable.setHorizontalAlignment(Element.ALIGN_CENTER);
            secondRowMasterPDFTable.setWidthPercentage(80);

            PdfPCell secondMasterHeaderCell;

            secondMasterHeaderCell = new PdfPCell(DOJPDFTable);
            secondMasterHeaderCell.setPadding(5);
            secondMasterHeaderCell.setBorder(Rectangle.NO_BORDER);
            secondMasterHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            secondRowMasterPDFTable.addCell(secondMasterHeaderCell);

            secondMasterHeaderCell = new PdfPCell(totalNBPDFTable);
            secondMasterHeaderCell.setPadding(5);
            secondMasterHeaderCell.setBorder(Rectangle.NO_BORDER);
            secondMasterHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            secondRowMasterPDFTable.addCell(secondMasterHeaderCell);

            if (rowCountRSM == 3) {
                PdfPTable NBRefundPDFTableLocal = new PdfPTable(2);
                NBRefundPDFTableLocal.setWidths(new float[]{9f, 9f});
                NBRefundPDFTableLocal.setSpacingBefore(3f);
                NBRefundPDFTableLocal.setWidthPercentage(100);

                PdfPCell NBRefundCellLocal = new PdfPCell(new Paragraph("NB Refund (in Cr.)", small_bold));
                NBRefundCellLocal.setBackgroundColor(NOPCellColor);
                NBRefundCellLocal.setHorizontalAlignment(Element.ALIGN_CENTER);
                NBRefundCellLocal.setPadding(5);
                NBRefundPDFTableLocal.addCell(NBRefundCellLocal);

                PdfPCell NBRefundNameCellLocal = new PdfPCell(new Paragraph(NB_REFUND + " Cr.", small_bold_title_name));
                NBRefundNameCellLocal.setBackgroundColor(userCodeCellColor);
                NBRefundNameCellLocal.setHorizontalAlignment(Element.ALIGN_CENTER);
                NBRefundNameCellLocal.setPadding(5);
                NBRefundPDFTableLocal.addCell(NBRefundNameCellLocal);


                secondMasterHeaderCell = new PdfPCell(NBRefundPDFTableLocal);
                secondMasterHeaderCell.setPadding(5);
                secondMasterHeaderCell.setBorder(Rectangle.NO_BORDER);
                secondMasterHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                secondRowMasterPDFTable.addCell(secondMasterHeaderCell);
            }

            document.add(secondRowMasterPDFTable);
            //End Main ROW Two

            //Start Main ROW Three
            //Add No of. Branch

            //Add User Name
            PdfPTable userNameRowTable = new PdfPTable(2);
            userNameRowTable.setSpacingBefore(3f);
            userNameRowTable.setWidths(new float[]{9f, 9f});
            userNameRowTable.setWidthPercentage(100);

            PdfPCell userNameTitleCell = new PdfPCell(new Paragraph(FIELD_2, small_bold));
            userNameTitleCell.setBackgroundColor(userCodeTitleCellColor);
            userNameTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            userNameTitleCell.setPadding(5);
            userNameRowTable.addCell(userNameTitleCell);

            PdfPCell userNameCell = new PdfPCell(new Paragraph(NAME, small_bold_title_name));
            userNameCell.setBackgroundColor(userCodeCellColor);
            userNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            userNameCell.setPadding(5);
            userNameRowTable.addCell(userNameCell);

            //Tenure(Yr) details
            PdfPTable tenurePDFTable = new PdfPTable(2);
            tenurePDFTable.setWidths(new float[]{9f, 9f});
            tenurePDFTable.setSpacingBefore(3f);
            tenurePDFTable.setWidthPercentage(100);

            PdfPCell tenureCell = new PdfPCell(new Paragraph("Tenure (Yr)", small_bold));
            tenureCell.setBackgroundColor(userCodeTitleCellColor);
            tenureCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tenureCell.setPadding(5);
            tenurePDFTable.addCell(tenureCell);

            PdfPCell tenureNameCell = new PdfPCell(new Paragraph(TENURE, small_bold_title_name));
            tenureNameCell.setBackgroundColor(userCodeCellColor);
            tenureNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tenureNameCell.setPadding(5);
            tenurePDFTable.addCell(tenureNameCell);

            //Total Renewals (in Cr.) Details
            PdfPTable totalRenewalsPDFTable = new PdfPTable(2);
            totalRenewalsPDFTable.setWidths(new float[]{9f, 9f});
            totalRenewalsPDFTable.setSpacingBefore(3f);
            totalRenewalsPDFTable.setWidthPercentage(100);

            PdfPCell totalRenewalsCell = new PdfPCell(new Paragraph("Total Renewals (in Cr.)", small_bold));
            totalRenewalsCell.setBackgroundColor(NOPCellColor);
            totalRenewalsCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalRenewalsCell.setPadding(5);
            totalRenewalsPDFTable.addCell(totalRenewalsCell);

            PdfPCell totalRenewalsNameCell = new PdfPCell(new Paragraph(TOTAL_RENEWALS + " Cr.", small_bold_title_name));
            totalRenewalsNameCell.setBackgroundColor(userCodeCellColor);
            totalRenewalsNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalRenewalsNameCell.setPadding(5);
            totalRenewalsPDFTable.addCell(totalRenewalsNameCell);

            //Total Surrender (in Cr.) Details
            PdfPTable totalSurrenderPDFTable = new PdfPTable(2);
            totalSurrenderPDFTable.setWidths(new float[]{9f, 9f});
            totalSurrenderPDFTable.setSpacingBefore(3f);
            totalSurrenderPDFTable.setWidthPercentage(100);

            PdfPCell totalSurrenderCell = new PdfPCell(new Paragraph("Total Surrender (in Cr.)", small_bold));
            totalSurrenderCell.setBackgroundColor(NOPCellColor);
            totalSurrenderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalSurrenderCell.setPadding(5);
            totalSurrenderPDFTable.addCell(totalSurrenderCell);

            PdfPCell totalSurrenderNameCell = new PdfPCell(new Paragraph(TOTAL_SURRENDER + " Cr.", small_bold_title_name));
            totalSurrenderNameCell.setBackgroundColor(userCodeCellColor);
            totalSurrenderNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalSurrenderNameCell.setPadding(5);
            totalSurrenderPDFTable.addCell(totalSurrenderNameCell);


            //Death Claim (in Cr.)
            PdfPTable deathClaimPDFTable = new PdfPTable(2);
            deathClaimPDFTable.setWidths(new float[]{9f, 9f});
            deathClaimPDFTable.setSpacingBefore(3f);
            deathClaimPDFTable.setWidthPercentage(100);

            PdfPCell deathClaimCell = new PdfPCell(new Paragraph("Death Claim (in Cr.)", small_bold));
            deathClaimCell.setBackgroundColor(NOPCellColor);
            deathClaimCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            deathClaimCell.setPadding(5);
            deathClaimPDFTable.addCell(deathClaimCell);

            PdfPCell deathClaimNameCell = new PdfPCell(new Paragraph(DEATH_CLAIM_AMOUNT + " Cr.", small_bold_title_name));
            deathClaimNameCell.setBackgroundColor(userCodeCellColor);
            deathClaimNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            deathClaimNameCell.setPadding(5);
            deathClaimPDFTable.addCell(deathClaimNameCell);

            //Main Table
            PdfPTable rowThreePDFTable = new PdfPTable(5);
            rowThreePDFTable.setSpacingBefore(3f);
            rowThreePDFTable.setWidthPercentage(100);

            PdfPCell thirdRowMasterHeaderCell;

            thirdRowMasterHeaderCell = new PdfPCell(userNameRowTable);
            thirdRowMasterHeaderCell.setPadding(5);
            thirdRowMasterHeaderCell.setBorder(Rectangle.NO_BORDER);
            thirdRowMasterHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            rowThreePDFTable.addCell(thirdRowMasterHeaderCell);

            thirdRowMasterHeaderCell = new PdfPCell(tenurePDFTable);
            thirdRowMasterHeaderCell.setPadding(5);
            thirdRowMasterHeaderCell.setBorder(Rectangle.NO_BORDER);
            thirdRowMasterHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            rowThreePDFTable.addCell(thirdRowMasterHeaderCell);

            thirdRowMasterHeaderCell = new PdfPCell(totalRenewalsPDFTable);
            thirdRowMasterHeaderCell.setPadding(5);
            thirdRowMasterHeaderCell.setBorder(Rectangle.NO_BORDER);
            thirdRowMasterHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            rowThreePDFTable.addCell(thirdRowMasterHeaderCell);

            thirdRowMasterHeaderCell = new PdfPCell(totalSurrenderPDFTable);
            thirdRowMasterHeaderCell.setPadding(5);
            thirdRowMasterHeaderCell.setBorder(Rectangle.NO_BORDER);
            thirdRowMasterHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            rowThreePDFTable.addCell(thirdRowMasterHeaderCell);

            thirdRowMasterHeaderCell = new PdfPCell(deathClaimPDFTable);
            thirdRowMasterHeaderCell.setPadding(5);
            thirdRowMasterHeaderCell.setBorder(Rectangle.NO_BORDER);
            thirdRowMasterHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            rowThreePDFTable.addCell(thirdRowMasterHeaderCell);

            document.add(rowThreePDFTable);
            //End Main ROW Three


            /////

            Paragraph leavePara = new Paragraph(" ");
            PdfPTable blankPDFTable = new PdfPTable(1);
            blankPDFTable.getDefaultCell().setBorder(0);
            blankPDFTable.addCell(leavePara);

            document.add(blankPDFTable);
            document.add(blankPDFTable);


            //Digital Param Part One
            //Row One
            PdfPTable digitalParameterTableRowOne = new PdfPTable(8);
            //NBParameterTableRowTwoPartTwo.setSpacingBefore(3f);
            digitalParameterTableRowOne.setWidths(new float[]{3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f});
            digitalParameterTableRowOne.setWidthPercentage(100);

            String digitalParamTitle = "Digital Parameters (Weightage - 20)";

            PdfPCell digParamCellOne = new PdfPCell(new Paragraph(digitalParamTitle, small_bold));
            digParamCellOne.setBackgroundColor(digParamCellOneColor);
            digParamCellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
            digParamCellOne.setPadding(5);
            digParamCellOne.setColspan(8);
            digitalParameterTableRowOne.addCell(digParamCellOne);

            PdfPCell digParamTableRowCellOne = new PdfPCell(new Paragraph("Digital Adoption % (Weightage 5)", small_bold));
            digParamTableRowCellOne.setBackgroundColor(digParamTableRowColor);
            digParamTableRowCellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
            digParamTableRowCellOne.setPadding(5);
            digParamTableRowCellOne.setColspan(2);
            digitalParameterTableRowOne.addCell(digParamTableRowCellOne);

            PdfPCell digParamTableRowCellTwo = new PdfPCell(new Paragraph("Smart Advisor\n" +
                    "Adoption %\n" +
                    "(Weightage 5)", small_bold));
            digParamTableRowCellTwo.setBackgroundColor(digParamTableRowColor);
            digParamTableRowCellTwo.setHorizontalAlignment(Element.ALIGN_CENTER);
            digParamTableRowCellTwo.setPadding(5);
            digParamTableRowCellTwo.setColspan(2);
            digitalParameterTableRowOne.addCell(digParamTableRowCellTwo);

            PdfPCell digParamTableRowCellThree = new PdfPCell(new Paragraph("E-Shiksha Empowered-Usage %\n" +
                    "(Weightage 5)", small_bold));
            digParamTableRowCellThree.setBackgroundColor(digParamTableRowColor);
            digParamTableRowCellThree.setHorizontalAlignment(Element.ALIGN_CENTER);
            digParamTableRowCellThree.setPadding(5);
            digParamTableRowCellThree.setColspan(2);
            digitalParameterTableRowOne.addCell(digParamTableRowCellThree);

            PdfPCell digParamTableRowCellFour;


            String tableRowTwoCellFourTitle = "Customer self Service & other's\n" + "%\n" + "(Weightage 5)";

            digParamTableRowCellFour = new PdfPCell(new Paragraph(tableRowTwoCellFourTitle, small_bold));
            digParamTableRowCellFour.setBackgroundColor(digParamTableRowColor);
            digParamTableRowCellFour.setHorizontalAlignment(Element.ALIGN_CENTER);
            digParamTableRowCellFour.setPadding(5);
            digParamTableRowCellFour.setColspan(2);
            digitalParameterTableRowOne.addCell(digParamTableRowCellFour);
            //end Row One


            //ThirdRow
            PdfPCell digparamRowThreeCellOne = new PdfPCell(new Paragraph("%Achieved", small_bold));
            digparamRowThreeCellOne.setBackgroundColor(digParamTableRowColor);
            digparamRowThreeCellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
            digparamRowThreeCellOne.setPadding(5);
            //NBParamRowThreeCellOne.setColspan(2);

            PdfPCell digParamRowThreeCellTwo = new PdfPCell(new Paragraph("Score", small_bold));
            digParamRowThreeCellTwo.setBackgroundColor(digParamTableRowColor);
            digParamRowThreeCellTwo.setHorizontalAlignment(Element.ALIGN_CENTER);
            digParamRowThreeCellTwo.setPadding(5);
            //NBParamRowThreeCellTwo.setColspan(2);
            digitalParameterTableRowOne.addCell(digparamRowThreeCellOne);
            digitalParameterTableRowOne.addCell(digParamRowThreeCellTwo);
            digitalParameterTableRowOne.addCell(digparamRowThreeCellOne);
            digitalParameterTableRowOne.addCell(digParamRowThreeCellTwo);
            digitalParameterTableRowOne.addCell(digparamRowThreeCellOne);
            digitalParameterTableRowOne.addCell(digParamRowThreeCellTwo);
            digitalParameterTableRowOne.addCell(digparamRowThreeCellOne);
            digitalParameterTableRowOne.addCell(digParamRowThreeCellTwo);

            //Fourth Row
            PdfPCell digparamRowThreeCellOneValue = new PdfPCell(new Paragraph(DIGITAL_ADOPT_ACHV + "%", small_bold_title_name));
            digparamRowThreeCellOneValue.setBackgroundColor(digParamTableRowColor);
            digparamRowThreeCellOneValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            digparamRowThreeCellOneValue.setPadding(5);
            digitalParameterTableRowOne.addCell(digparamRowThreeCellOneValue);


            PdfPCell digParamRowThreeCellTwoValue = new PdfPCell(new Paragraph(DIGITAL_ADOPT_SCORE, small_bold_title_name));
            digParamRowThreeCellTwoValue.setBackgroundColor(digParamTableRowColor);
            digParamRowThreeCellTwoValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            digParamRowThreeCellTwoValue.setPadding(5);
            digitalParameterTableRowOne.addCell(digParamRowThreeCellTwoValue);

            PdfPCell digParamRowSevenCellThreeValue = new PdfPCell(new Paragraph(SMART_ADV_ADOP_ACHV + "%", small_bold_title_name));
            digParamRowSevenCellThreeValue.setBackgroundColor(digParamTableRowColor);
            digParamRowSevenCellThreeValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            digParamRowSevenCellThreeValue.setPadding(5);
            //NBParamRowThreeCellTwo.setColspan(2);
            digitalParameterTableRowOne.addCell(digParamRowSevenCellThreeValue);

            PdfPCell digParamRowSevenCellFourValue = new PdfPCell(new Paragraph(SMART_ADV_ADOP_SCORE, small_bold_title_name));
            digParamRowSevenCellFourValue.setBackgroundColor(digParamTableRowColor);
            digParamRowSevenCellFourValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            digParamRowSevenCellFourValue.setPadding(5);
            //NBParamRowThreeCellTwo.setColspan(2);
            digitalParameterTableRowOne.addCell(digParamRowSevenCellFourValue);

            PdfPCell digParamRowSevenCellFIveValue = new PdfPCell(new Paragraph(ESHIKSHA_USAGE_ACHV + "%", small_bold_title_name));
            digParamRowSevenCellFIveValue.setBackgroundColor(digParamTableRowColor);
            digParamRowSevenCellFIveValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            digParamRowSevenCellFIveValue.setPadding(5);
            //NBParamRowThreeCellTwo.setColspan(2);
            digitalParameterTableRowOne.addCell(digParamRowSevenCellFIveValue);

            PdfPCell digParamRowSevenCellSixValue = new PdfPCell(new Paragraph(ESHIKSHA_USAGE_SCORE, small_bold_title_name));
            digParamRowSevenCellSixValue.setBackgroundColor(digParamTableRowColor);
            digParamRowSevenCellSixValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            digParamRowSevenCellSixValue.setPadding(5);
            //NBParamRowThreeCellTwo.setColspan(2);
            digitalParameterTableRowOne.addCell(digParamRowSevenCellSixValue);

            PdfPCell digParamRowSevenCellSevenValue = new PdfPCell(new Paragraph(CUSTOMER_SELFSRV_ACHV + "%", small_bold_title_name));
            digParamRowSevenCellSevenValue.setBackgroundColor(digParamTableRowColor);
            digParamRowSevenCellSevenValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            digParamRowSevenCellSevenValue.setPadding(5);
            //NBParamRowThreeCellTwo.setColspan(2);
            digitalParameterTableRowOne.addCell(digParamRowSevenCellSevenValue);

            PdfPCell digParamRowThreeCellEightValue = new PdfPCell(new Paragraph(CUSTOMER_SELFSRV_SCORE, small_bold_title_name));
            digParamRowThreeCellEightValue.setBackgroundColor(digParamTableRowColor);
            digParamRowThreeCellEightValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            digParamRowThreeCellEightValue.setPadding(5);
            //NBParamRowThreeCellTwo.setColspan(2);
            digitalParameterTableRowOne.addCell(digParamRowThreeCellEightValue);


            //End FourthRow

            //end ThirdRow
            //End Row One part 1

            //Customer Servicing Parameters (Weightage 25)
            //Row One Part Two
            //Row One
            PdfPTable busQualityParameterTableRowOne = new PdfPTable(10);
            busQualityParameterTableRowOne.setWidths(new float[]{3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f});
            busQualityParameterTableRowOne.setWidthPercentage(100);

            PdfPCell busQualityParamCellOne = new PdfPCell(new Paragraph("Customer Servicing Parameters (Weightage 25)", small_bold));
            busQualityParamCellOne.setBackgroundColor(digParamCellOneColor);
            busQualityParamCellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
            busQualityParamCellOne.setPadding(5);
            busQualityParamCellOne.setColspan(10);
            busQualityParameterTableRowOne.addCell(busQualityParamCellOne);

            PdfPCell busQualityParamTableRowCellOne = new PdfPCell(new Paragraph("Early Claims Count (Weightage 5)", small_bold));
            busQualityParamTableRowCellOne.setBackgroundColor(busQualityParamTableRowColor);
            busQualityParamTableRowCellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
            busQualityParamTableRowCellOne.setPadding(5);
            busQualityParamTableRowCellOne.setColspan(2);
            busQualityParameterTableRowOne.addCell(busQualityParamTableRowCellOne);

            PdfPCell busQualityParamTableRowCellTwo = new PdfPCell(new Paragraph("Misselling Complaints Count (Weightage 5)", small_bold));
            busQualityParamTableRowCellTwo.setBackgroundColor(busQualityParamTableRowColor);
            busQualityParamTableRowCellTwo.setHorizontalAlignment(Element.ALIGN_CENTER);
            busQualityParamTableRowCellTwo.setPadding(5);
            busQualityParamTableRowCellTwo.setColspan(2);
            busQualityParameterTableRowOne.addCell(busQualityParamTableRowCellTwo);

            PdfPCell busQualityParamTableRowCellThree = new PdfPCell(new Paragraph("FLC Vs Issuance % (Weightage 5)", small_bold));
            busQualityParamTableRowCellThree.setBackgroundColor(busQualityParamTableRowColor);
            busQualityParamTableRowCellThree.setHorizontalAlignment(Element.ALIGN_CENTER);
            busQualityParamTableRowCellThree.setPadding(5);
            busQualityParamTableRowCellThree.setColspan(2);
            busQualityParameterTableRowOne.addCell(busQualityParamTableRowCellThree);

            PdfPCell busQualityParamTableRowCellFour = new PdfPCell(new Paragraph("PNR Complaints (Count) (Weightage 5)", small_bold));
            busQualityParamTableRowCellFour.setBackgroundColor(busQualityParamTableRowColor);
            busQualityParamTableRowCellFour.setHorizontalAlignment(Element.ALIGN_CENTER);
            busQualityParamTableRowCellFour.setPadding(5);
            busQualityParamTableRowCellFour.setColspan(2);
            busQualityParameterTableRowOne.addCell(busQualityParamTableRowCellFour);

            PdfPCell busQualityParamTableRowCellFive = new PdfPCell(new Paragraph("NPS Score (Weightage 5)", small_bold));
            busQualityParamTableRowCellFive.setBackgroundColor(busQualityParamTableRowColor);
            busQualityParamTableRowCellFive.setHorizontalAlignment(Element.ALIGN_CENTER);
            busQualityParamTableRowCellFive.setPadding(5);
            busQualityParamTableRowCellFive.setColspan(2);
            busQualityParameterTableRowOne.addCell(busQualityParamTableRowCellFive);

            //end Row One


            //ThirdRow
            PdfPCell busQualityparamRowThreeCellOne = new PdfPCell(new Paragraph("Count", small_bold));
            busQualityparamRowThreeCellOne.setBackgroundColor(busQualityParamTableRowColor);
            busQualityparamRowThreeCellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
            busQualityparamRowThreeCellOne.setPadding(5);
            //NBParamRowThreeCellOne.setColspan(2);

            PdfPCell busQualityParamRowThreeCellTwo = new PdfPCell(new Paragraph("Score", small_bold));
            busQualityParamRowThreeCellTwo.setBackgroundColor(busQualityParamTableRowColor);
            busQualityParamRowThreeCellTwo.setHorizontalAlignment(Element.ALIGN_CENTER);
            busQualityParamRowThreeCellTwo.setPadding(5);
            //NBParamRowThreeCellTwo.setColspan(2);
            busQualityParameterTableRowOne.addCell(busQualityparamRowThreeCellOne);
            busQualityParameterTableRowOne.addCell(busQualityParamRowThreeCellTwo);
            busQualityParameterTableRowOne.addCell(busQualityparamRowThreeCellOne);
            busQualityParameterTableRowOne.addCell(busQualityParamRowThreeCellTwo);

            busQualityparamRowThreeCellOne = new PdfPCell(new Paragraph("% Achieved", small_bold));
            busQualityparamRowThreeCellOne.setBackgroundColor(busQualityParamTableRowColor);
            busQualityparamRowThreeCellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
            busQualityparamRowThreeCellOne.setPadding(5);
            busQualityParameterTableRowOne.addCell(busQualityparamRowThreeCellOne);
            busQualityParameterTableRowOne.addCell(busQualityParamRowThreeCellTwo);

            busQualityparamRowThreeCellOne = new PdfPCell(new Paragraph("Count", small_bold));
            busQualityparamRowThreeCellOne.setBackgroundColor(busQualityParamTableRowColor);
            busQualityparamRowThreeCellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
            busQualityparamRowThreeCellOne.setPadding(5);
            busQualityParameterTableRowOne.addCell(busQualityparamRowThreeCellOne);
            busQualityParameterTableRowOne.addCell(busQualityParamRowThreeCellTwo);
            busQualityParameterTableRowOne.addCell(busQualityparamRowThreeCellOne);
            busQualityParameterTableRowOne.addCell(busQualityParamRowThreeCellTwo);

            //Fourth Row
            PdfPCell busQualityparamRowThreeCellOneValue = new PdfPCell(new Paragraph(EARLY_DEATH_CLAIM_ACHV, small_bold_title_name));
            busQualityparamRowThreeCellOneValue.setBackgroundColor(busQualityParamTableRowColor);
            busQualityparamRowThreeCellOneValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            busQualityparamRowThreeCellOneValue.setPadding(5);
            //NBParamRowThreeCellOne.setColspan(2);
            busQualityParameterTableRowOne.addCell(busQualityparamRowThreeCellOneValue);


            PdfPCell busQualityParamRowThreeCellTwoValue = new PdfPCell(new Paragraph(EARLY_DEATH_CLAIM_SCORE, small_bold_title_name));
            busQualityParamRowThreeCellTwoValue.setBackgroundColor(busQualityParamTableRowColor);
            busQualityParamRowThreeCellTwoValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            busQualityParamRowThreeCellTwoValue.setPadding(5);
            //NBParamRowThreeCellTwo.setColspan(2);
            busQualityParameterTableRowOne.addCell(busQualityParamRowThreeCellTwoValue);

            PdfPCell busQualityParamRowSevenCellThreeValue = new PdfPCell(new Paragraph(MIS_SELL_ACHV, small_bold_title_name));
            busQualityParamRowSevenCellThreeValue.setBackgroundColor(busQualityParamTableRowColor);
            busQualityParamRowSevenCellThreeValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            busQualityParamRowSevenCellThreeValue.setPadding(5);
            //NBParamRowThreeCellTwo.setColspan(2);
            busQualityParameterTableRowOne.addCell(busQualityParamRowSevenCellThreeValue);

            PdfPCell busQualityRowSevenCellFourValue = new PdfPCell(new Paragraph(MIS_SELL_SCORE, small_bold_title_name));
            busQualityRowSevenCellFourValue.setBackgroundColor(busQualityParamTableRowColor);
            busQualityRowSevenCellFourValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            busQualityRowSevenCellFourValue.setPadding(5);
            //NBParamRowThreeCellTwo.setColspan(2);
            busQualityParameterTableRowOne.addCell(busQualityRowSevenCellFourValue);

            PdfPCell busQualityParamRowSevenCellFIveValue = new PdfPCell(new Paragraph(FLC_ISSUENCE_ACHV + "%", small_bold_title_name));
            busQualityParamRowSevenCellFIveValue.setBackgroundColor(busQualityParamTableRowColor);
            busQualityParamRowSevenCellFIveValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            busQualityParamRowSevenCellFIveValue.setPadding(5);
            //NBParamRowThreeCellTwo.setColspan(2);
            busQualityParameterTableRowOne.addCell(busQualityParamRowSevenCellFIveValue);

            PdfPCell busQualityParamRowSevenCellSixValue = new PdfPCell(new Paragraph(FLC_ISSUENCE_SCORE, small_bold_title_name));
            busQualityParamRowSevenCellSixValue.setBackgroundColor(busQualityParamTableRowColor);
            busQualityParamRowSevenCellSixValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            busQualityParamRowSevenCellSixValue.setPadding(5);
            //NBParamRowThreeCellTwo.setColspan(2);
            busQualityParameterTableRowOne.addCell(busQualityParamRowSevenCellSixValue);

            PdfPCell PNRCellOneValue = new PdfPCell(new Paragraph(PNR_COUNT_ACHV, small_bold_title_name));
            PNRCellOneValue.setBackgroundColor(busQualityParamTableRowColor);
            PNRCellOneValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            PNRCellOneValue.setPadding(5);
            busQualityParameterTableRowOne.addCell(PNRCellOneValue);

            PdfPCell PNRCellTWOValue = new PdfPCell(new Paragraph(PNR_COUNT_SCORE, small_bold_title_name));
            PNRCellTWOValue.setBackgroundColor(busQualityParamTableRowColor);
            PNRCellTWOValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            PNRCellTWOValue.setPadding(5);
            busQualityParameterTableRowOne.addCell(PNRCellTWOValue);

            PdfPCell NPSSCoreCellOneValue = new PdfPCell(new Paragraph(NPS_ACHV, small_bold_title_name));
            NPSSCoreCellOneValue.setBackgroundColor(busQualityParamTableRowColor);
            NPSSCoreCellOneValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            NPSSCoreCellOneValue.setPadding(5);
            busQualityParameterTableRowOne.addCell(NPSSCoreCellOneValue);

            PdfPCell NPSSCoreCellTwoValue = new PdfPCell(new Paragraph(NPS_SCORE, small_bold_title_name));
            NPSSCoreCellTwoValue.setBackgroundColor(busQualityParamTableRowColor);
            NPSSCoreCellTwoValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            NPSSCoreCellTwoValue.setPadding(5);
            busQualityParameterTableRowOne.addCell(NPSSCoreCellTwoValue);

            //End FourthRow
            //End Row One part 2

            //Main Table
            PdfPTable digitalNBMainTable = new PdfPTable(2);
            digitalNBMainTable.setSpacingBefore(2f);
            digitalNBMainTable.setWidthPercentage(100);

            PdfPCell digitalNBHeaderCell;

            digitalNBHeaderCell = new PdfPCell(digitalParameterTableRowOne);
            digitalNBHeaderCell.setPadding(5);
            digitalNBHeaderCell.setBorder(Rectangle.NO_BORDER);
            digitalNBHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            digitalNBMainTable.addCell(digitalNBHeaderCell);

            digitalNBHeaderCell = new PdfPCell(busQualityParameterTableRowOne);
            digitalNBHeaderCell.setPadding(5);
            digitalNBHeaderCell.setBorder(Rectangle.NO_BORDER);
            digitalNBHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            digitalNBMainTable.addCell(digitalNBHeaderCell);

            document.add(digitalNBMainTable);

            //NB parameter Part Two
            //End Digital Param Part One

            document.add(blankPDFTable);
            document.add(blankPDFTable);

            //Business Quality Parameters

            //NB Parameter Details
            //Row Two part 1
            PdfPTable NBParameterTableRowTwoPartTwo = new PdfPTable(12);
            NBParameterTableRowTwoPartTwo.setWidths(new float[]{3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f});
            NBParameterTableRowTwoPartTwo.setWidthPercentage(100);

            String NBParamTitle = "NB Parameters (Weightage 35)";

            PdfPCell NBParamCellOne = new PdfPCell(new Paragraph(NBParamTitle, small_bold));
            NBParamCellOne.setBackgroundColor(digParamCellOneColor);
            NBParamCellOne.setPadding(5);
            NBParamCellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
            NBParamCellOne.setColspan(12);
            NBParameterTableRowTwoPartTwo.addCell(NBParamCellOne);

            PdfPCell NBParameterTableRowCellOne = new PdfPCell(new Paragraph("FTR Ratio % (Weightage 10)", small_bold));
            NBParameterTableRowCellOne.setBackgroundColor(NBParameterTableRowColor);
            NBParameterTableRowCellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
            NBParameterTableRowCellOne.setPadding(5);
            NBParameterTableRowCellOne.setColspan(2);
            NBParameterTableRowTwoPartTwo.addCell(NBParameterTableRowCellOne);

            PdfPCell NBParameterTableRowCellTwo = new PdfPCell(new Paragraph("NB Refund Ratio % (Weightage 5)", small_bold));
            NBParameterTableRowCellTwo.setBackgroundColor(NBParameterTableRowColor);
            NBParameterTableRowCellTwo.setHorizontalAlignment(Element.ALIGN_CENTER);
            NBParameterTableRowCellTwo.setPadding(5);
            NBParameterTableRowCellTwo.setColspan(2);
            NBParameterTableRowTwoPartTwo.addCell(NBParameterTableRowCellTwo);

            PdfPCell NBParameterTableRowCellThree = new PdfPCell(new Paragraph(tableThreePageOneMainCellThree, small_bold));
            NBParameterTableRowCellThree.setBackgroundColor(NBParameterTableRowColor);
            NBParameterTableRowCellThree.setHorizontalAlignment(Element.ALIGN_CENTER);
            NBParameterTableRowCellThree.setPadding(5);
            NBParameterTableRowCellThree.setColspan(2);
            NBParameterTableRowTwoPartTwo.addCell(NBParameterTableRowCellThree);

            PdfPCell NBParameterTableRowCellFour = new PdfPCell(new Paragraph("Requirement Closure TAT (Average days) (Weightage 5)", small_bold));
            NBParameterTableRowCellFour.setBackgroundColor(NBParameterTableRowColor);
            NBParameterTableRowCellFour.setHorizontalAlignment(Element.ALIGN_CENTER);
            NBParameterTableRowCellFour.setPadding(5);
            NBParameterTableRowCellFour.setColspan(2);
            NBParameterTableRowTwoPartTwo.addCell(NBParameterTableRowCellFour);

            PdfPCell NBParameterTableRowCellFive = new PdfPCell(new Paragraph("PIWC Mismatch(Count) (Weightage 5)", small_bold));
            NBParameterTableRowCellFive.setBackgroundColor(NBParameterTableRowColor);
            NBParameterTableRowCellFive.setHorizontalAlignment(Element.ALIGN_CENTER);
            NBParameterTableRowCellFive.setPadding(5);
            NBParameterTableRowCellFive.setColspan(2);
            NBParameterTableRowTwoPartTwo.addCell(NBParameterTableRowCellFive);


            PdfPCell NBParameterTableRowCellSix = new PdfPCell(new Paragraph("PIWC Clearance Efficiency (Weightage 5)", small_bold));
            NBParameterTableRowCellSix.setBackgroundColor(NBParameterTableRowColor);
            NBParameterTableRowCellSix.setHorizontalAlignment(Element.ALIGN_CENTER);
            NBParameterTableRowCellSix.setPadding(5);
            NBParameterTableRowCellSix.setColspan(2);
            NBParameterTableRowTwoPartTwo.addCell(NBParameterTableRowCellSix);

            //ThirdRow
            PdfPCell NBParamRowThreeCellOne = new PdfPCell(new Paragraph("%Achieved", small_bold));
            NBParamRowThreeCellOne.setBackgroundColor(NBParameterTableRowColor);
            NBParamRowThreeCellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
            NBParamRowThreeCellOne.setPadding(5);

            PdfPCell NBParamRowThreeCellTwo = new PdfPCell(new Paragraph("Score", small_bold));
            NBParamRowThreeCellTwo.setBackgroundColor(NBParameterTableRowColor);
            NBParamRowThreeCellTwo.setHorizontalAlignment(Element.ALIGN_CENTER);
            NBParamRowThreeCellTwo.setPadding(5);
            NBParameterTableRowTwoPartTwo.addCell(NBParamRowThreeCellOne);
            NBParameterTableRowTwoPartTwo.addCell(NBParamRowThreeCellTwo);
            NBParameterTableRowTwoPartTwo.addCell(NBParamRowThreeCellOne);
            NBParameterTableRowTwoPartTwo.addCell(NBParamRowThreeCellTwo);
            NBParameterTableRowTwoPartTwo.addCell(NBParamRowThreeCellOne);
            NBParameterTableRowTwoPartTwo.addCell(NBParamRowThreeCellTwo);

            NBParamRowThreeCellOne = new PdfPCell(new Paragraph("Achieved", small_bold));
            NBParamRowThreeCellOne.setBackgroundColor(NBParameterTableRowColor);
            NBParamRowThreeCellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
            NBParamRowThreeCellOne.setPadding(5);
            NBParameterTableRowTwoPartTwo.addCell(NBParamRowThreeCellOne);
            NBParameterTableRowTwoPartTwo.addCell(NBParamRowThreeCellTwo);
            NBParameterTableRowTwoPartTwo.addCell(NBParamRowThreeCellOne);
            NBParameterTableRowTwoPartTwo.addCell(NBParamRowThreeCellTwo);

            NBParamRowThreeCellOne = new PdfPCell(new Paragraph("%Achieved", small_bold));
            NBParamRowThreeCellOne.setBackgroundColor(NBParameterTableRowColor);
            NBParamRowThreeCellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
            NBParamRowThreeCellOne.setPadding(5);

            NBParameterTableRowTwoPartTwo.addCell(NBParamRowThreeCellOne);
            NBParameterTableRowTwoPartTwo.addCell(NBParamRowThreeCellTwo);
            //end ThirdRow

            //Fourth Row
            PdfPCell NBParamRowThreeCellOneValue = new PdfPCell(new Paragraph(FTR_ACHV + "%", small_bold_title_name));
            NBParamRowThreeCellOneValue.setBackgroundColor(NBParameterTableRowColor);
            NBParamRowThreeCellOneValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            NBParamRowThreeCellOneValue.setPadding(5);
            //NBParamRowThreeCellOne.setColspan(2);
            NBParameterTableRowTwoPartTwo.addCell(NBParamRowThreeCellOneValue);


            PdfPCell NBParamRowThreeCellTwoValue = new PdfPCell(new Paragraph(FTR_SCORE, small_bold_title_name));
            NBParamRowThreeCellTwoValue.setBackgroundColor(NBParameterTableRowColor);
            NBParamRowThreeCellTwoValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            NBParamRowThreeCellTwoValue.setPadding(5);
            //NBParamRowThreeCellTwo.setColspan(2);
            NBParameterTableRowTwoPartTwo.addCell(NBParamRowThreeCellTwoValue);

            PdfPCell NBParamRowSevenCellThreeValue = new PdfPCell(new Paragraph(NB_REFUND_ACHV + "%", small_bold_title_name));
            NBParamRowSevenCellThreeValue.setBackgroundColor(NBParameterTableRowColor);
            NBParamRowSevenCellThreeValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            NBParamRowSevenCellThreeValue.setPadding(5);
            //NBParamRowThreeCellTwo.setColspan(2);
            NBParameterTableRowTwoPartTwo.addCell(NBParamRowSevenCellThreeValue);

            PdfPCell NBParamRowSevenCellFourValue = new PdfPCell(new Paragraph(NB_REFUND_SCORE, small_bold_title_name));
            NBParamRowSevenCellFourValue.setBackgroundColor(NBParameterTableRowColor);
            NBParamRowSevenCellFourValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            NBParamRowSevenCellFourValue.setPadding(5);
            //NBParamRowThreeCellTwo.setColspan(2);
            NBParameterTableRowTwoPartTwo.addCell(NBParamRowSevenCellFourValue);

            String achieveTable3ColMain3 = "", scoreTable3ColMain3 = "";
            if (rowCountRSM == 3) {
                achieveTable3ColMain3 = ONLINE_PAYMENT_ACHV;
                scoreTable3ColMain3 = ONLINE_PAYMENT_SCORE;
            } else {
                //For Banca
                achieveTable3ColMain3 = GROUP_REFUND_ACHV;
                scoreTable3ColMain3 = GROUP_REFUND_SCORE;
            }

            PdfPCell NBParamRowSevenCellFIveValue = new PdfPCell(new Paragraph(achieveTable3ColMain3 + "%", small_bold_title_name));
            NBParamRowSevenCellFIveValue.setBackgroundColor(NBParameterTableRowColor);
            NBParamRowSevenCellFIveValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            NBParamRowSevenCellFIveValue.setPadding(5);
            NBParameterTableRowTwoPartTwo.addCell(NBParamRowSevenCellFIveValue);

            PdfPCell NBParamRowSevenCellSixValue = new PdfPCell(new Paragraph(scoreTable3ColMain3, small_bold_title_name));
            NBParamRowSevenCellSixValue.setBackgroundColor(NBParameterTableRowColor);
            NBParamRowSevenCellSixValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            NBParamRowSevenCellSixValue.setPadding(5);
            NBParameterTableRowTwoPartTwo.addCell(NBParamRowSevenCellSixValue);

            PdfPCell NBParamRowSevenCellSevenValue = new PdfPCell(new Paragraph(REQUIREMENT_PENDING_ACHV, small_bold_title_name));
            NBParamRowSevenCellSevenValue.setBackgroundColor(NBParameterTableRowColor);
            NBParamRowSevenCellSevenValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            NBParamRowSevenCellSevenValue.setPadding(5);
            NBParameterTableRowTwoPartTwo.addCell(NBParamRowSevenCellSevenValue);

            PdfPCell NBParamRowThreeCellEightValue = new PdfPCell(new Paragraph(REQUIREMENT_PENDING_SCORE, small_bold_title_name));
            NBParamRowThreeCellEightValue.setBackgroundColor(NBParameterTableRowColor);
            NBParamRowThreeCellEightValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            NBParamRowThreeCellEightValue.setPadding(5);
            NBParameterTableRowTwoPartTwo.addCell(NBParamRowThreeCellEightValue);


            PdfPCell piwcMismatchAchieveValue = new PdfPCell(new Paragraph(PIWC_MISMATCH_ACHV, small_bold_title_name));
            piwcMismatchAchieveValue.setBackgroundColor(NBParameterTableRowColor);
            piwcMismatchAchieveValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            piwcMismatchAchieveValue.setPadding(5);
            NBParameterTableRowTwoPartTwo.addCell(piwcMismatchAchieveValue);

            PdfPCell piwcMismatchScoreValue = new PdfPCell(new Paragraph(PIWC_MISMATCH_SCORE, small_bold_title_name));
            piwcMismatchScoreValue.setBackgroundColor(NBParameterTableRowColor);
            piwcMismatchScoreValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            piwcMismatchScoreValue.setPadding(5);
            NBParameterTableRowTwoPartTwo.addCell(piwcMismatchScoreValue);

            PdfPCell piwcClearAchieveValue = new PdfPCell(new Paragraph(PIWC_EFFICIENCY_ACHV + "%", small_bold_title_name));
            piwcClearAchieveValue.setBackgroundColor(NBParameterTableRowColor);
            piwcClearAchieveValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            piwcClearAchieveValue.setPadding(5);
            NBParameterTableRowTwoPartTwo.addCell(piwcClearAchieveValue);

            PdfPCell piwcClearScoreValue = new PdfPCell(new Paragraph(PIWC_EFFICIENCY_SCORE, small_bold_title_name));
            piwcClearScoreValue.setBackgroundColor(NBParameterTableRowColor);
            piwcClearScoreValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            piwcClearScoreValue.setPadding(5);
            NBParameterTableRowTwoPartTwo.addCell(piwcClearScoreValue);

            //End FourthRow

            //end Row One Part 2

            //PS Parameters Details
            //Row Two part 2
            PdfPTable PSParameterTableRowTwoPartTwo = new PdfPTable(8);
            PSParameterTableRowTwoPartTwo.setWidths(new float[]{3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f});
            PSParameterTableRowTwoPartTwo.setWidthPercentage(100);


            PdfPCell PSParamCellOne = new PdfPCell(new Paragraph("Persistency Parameters (Weightage 20)", small_bold));
            PSParamCellOne.setBackgroundColor(digParamCellOneColor);
            PSParamCellOne.setPadding(5);
            PSParamCellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
            PSParamCellOne.setColspan(8);
            PSParameterTableRowTwoPartTwo.addCell(PSParamCellOne);


            PdfPCell PSParameterTableRowCellOne = new PdfPCell(new Paragraph("13th Month Persistency (Weightage 5)", small_bold));
            PSParameterTableRowCellOne.setBackgroundColor(PSParameterTableRowColor);
            PSParameterTableRowCellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
            PSParameterTableRowCellOne.setPadding(5);
            PSParameterTableRowCellOne.setColspan(2);
            PSParameterTableRowTwoPartTwo.addCell(PSParameterTableRowCellOne);

            PdfPCell PSParameterTableRowCellTwo = new PdfPCell(new Paragraph("61st Month Persistency % (Weightage 5)", small_bold));
            PSParameterTableRowCellTwo.setBackgroundColor(PSParameterTableRowColor);
            PSParameterTableRowCellTwo.setHorizontalAlignment(Element.ALIGN_CENTER);
            PSParameterTableRowCellTwo.setPadding(5);
            PSParameterTableRowCellTwo.setColspan(2);
            PSParameterTableRowTwoPartTwo.addCell(PSParameterTableRowCellTwo);

            PdfPCell PSParameterTableRowCellThree = new PdfPCell(new Paragraph("YTD Renewal Budget Achievement (Weightage 5)", small_bold));
            PSParameterTableRowCellThree.setBackgroundColor(PSParameterTableRowColor);
            PSParameterTableRowCellThree.setHorizontalAlignment(Element.ALIGN_CENTER);
            PSParameterTableRowCellThree.setPadding(5);
            PSParameterTableRowCellThree.setColspan(2);
            PSParameterTableRowTwoPartTwo.addCell(PSParameterTableRowCellThree);

            PdfPCell PSParameterTableRowCellFour = new PdfPCell(new Paragraph("Alternate Mode Registration% (Weightage 5)", small_bold));
            PSParameterTableRowCellFour.setBackgroundColor(PSParameterTableRowColor);
            PSParameterTableRowCellFour.setHorizontalAlignment(Element.ALIGN_CENTER);
            PSParameterTableRowCellFour.setPadding(5);
            PSParameterTableRowCellFour.setColspan(2);
            PSParameterTableRowTwoPartTwo.addCell(PSParameterTableRowCellFour);


            //ThirdRow
            PdfPCell PSParamRowThreeCellOne = new PdfPCell(new Paragraph("%Achieved", small_bold));
            PSParamRowThreeCellOne.setBackgroundColor(PSParameterTableRowColor);
            PSParamRowThreeCellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
            PSParamRowThreeCellOne.setPadding(5);

            PdfPCell PSParamRowThreeCellTwo = new PdfPCell(new Paragraph("Score", small_bold));
            PSParamRowThreeCellTwo.setBackgroundColor(PSParameterTableRowColor);
            PSParamRowThreeCellTwo.setHorizontalAlignment(Element.ALIGN_CENTER);
            PSParamRowThreeCellTwo.setPadding(5);
            PSParameterTableRowTwoPartTwo.addCell(PSParamRowThreeCellOne);
            PSParameterTableRowTwoPartTwo.addCell(PSParamRowThreeCellTwo);
            PSParameterTableRowTwoPartTwo.addCell(PSParamRowThreeCellOne);
            PSParameterTableRowTwoPartTwo.addCell(PSParamRowThreeCellTwo);
            PSParameterTableRowTwoPartTwo.addCell(PSParamRowThreeCellOne);
            PSParameterTableRowTwoPartTwo.addCell(PSParamRowThreeCellTwo);
            PSParameterTableRowTwoPartTwo.addCell(PSParamRowThreeCellOne);
            PSParameterTableRowTwoPartTwo.addCell(PSParamRowThreeCellTwo);
            //end ThirdRow

            //Fourth Row
            PdfPCell PSParamRowThreeCellOneValue = new PdfPCell(new Paragraph(PERSISTENCY_13 + "%", small_bold_title_name));
            PSParamRowThreeCellOneValue.setBackgroundColor(PSParameterTableRowColor);
            PSParamRowThreeCellOneValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            PSParamRowThreeCellOneValue.setPadding(5);
            PSParameterTableRowTwoPartTwo.addCell(PSParamRowThreeCellOneValue);


            PdfPCell PSParamRowThreeCellTwoValue = new PdfPCell(new Paragraph(PERSISTENCY_13_SCORE, small_bold_title_name));
            PSParamRowThreeCellTwoValue.setBackgroundColor(PSParameterTableRowColor);
            PSParamRowThreeCellTwoValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            PSParamRowThreeCellTwoValue.setPadding(5);
            PSParameterTableRowTwoPartTwo.addCell(PSParamRowThreeCellTwoValue);

            PdfPCell PSParamRowThreeCellThreeValue = new PdfPCell(new Paragraph(PERSISTENCY_61 + "%", small_bold_title_name));
            PSParamRowThreeCellThreeValue.setBackgroundColor(PSParameterTableRowColor);
            PSParamRowThreeCellThreeValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            PSParamRowThreeCellThreeValue.setPadding(5);
            PSParameterTableRowTwoPartTwo.addCell(PSParamRowThreeCellThreeValue);

            PdfPCell PSParamRowThreeCellFourValue = new PdfPCell(new Paragraph(PERSISTENCY_61_SCORE, small_bold_title_name));
            PSParamRowThreeCellFourValue.setBackgroundColor(PSParameterTableRowColor);
            PSParamRowThreeCellFourValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            PSParamRowThreeCellFourValue.setPadding(5);
            PSParameterTableRowTwoPartTwo.addCell(PSParamRowThreeCellFourValue);

            PdfPCell PSParamRowThreeCellFIveValue = new PdfPCell(new Paragraph(YTD_RENEWAL_ACHV + "%", small_bold_title_name));
            PSParamRowThreeCellFIveValue.setBackgroundColor(PSParameterTableRowColor);
            PSParamRowThreeCellFIveValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            PSParamRowThreeCellFIveValue.setPadding(5);
            PSParameterTableRowTwoPartTwo.addCell(PSParamRowThreeCellFIveValue);

            PdfPCell PSParamRowThreeCellSixValue = new PdfPCell(new Paragraph(YTD_RENEWAL_SCORE, small_bold_title_name));
            PSParamRowThreeCellSixValue.setBackgroundColor(PSParameterTableRowColor);
            PSParamRowThreeCellSixValue.setHorizontalAlignment(Element.ALIGN_CENTER);
            PSParamRowThreeCellSixValue.setPadding(5);
            PSParameterTableRowTwoPartTwo.addCell(PSParamRowThreeCellSixValue);


            PdfPCell PSParamRowThreeCellSeven = new PdfPCell(new Paragraph(ALT_MODE_REG_ACHV + "%", small_bold_title_name));
            PSParamRowThreeCellSeven.setBackgroundColor(PSParameterTableRowColor);
            PSParamRowThreeCellSeven.setHorizontalAlignment(Element.ALIGN_CENTER);
            PSParamRowThreeCellSeven.setPadding(5);
            PSParameterTableRowTwoPartTwo.addCell(PSParamRowThreeCellSeven);

            PdfPCell PSParamRowThreeCellEight = new PdfPCell(new Paragraph(ALT_MODE_REG_SCORE, small_bold_title_name));
            PSParamRowThreeCellEight.setBackgroundColor(PSParameterTableRowColor);
            PSParamRowThreeCellEight.setHorizontalAlignment(Element.ALIGN_CENTER);
            PSParamRowThreeCellEight.setPadding(5);
            PSParameterTableRowTwoPartTwo.addCell(PSParamRowThreeCellEight);
            //End FourthRow

            //end Row One Part 2

            //Main Table
            PdfPTable businessPSMainTable = new PdfPTable(2);
            businessPSMainTable.setSpacingBefore(2f);
            businessPSMainTable.setWidthPercentage(100);

            PdfPCell businessPSHeaderCell;

            businessPSHeaderCell = new PdfPCell(NBParameterTableRowTwoPartTwo);
            businessPSHeaderCell.setPadding(5);
            businessPSHeaderCell.setBorder(Rectangle.NO_BORDER);
            businessPSHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            businessPSMainTable.addCell(businessPSHeaderCell);

            businessPSHeaderCell = new PdfPCell(PSParameterTableRowTwoPartTwo);
            businessPSHeaderCell.setPadding(5);
            businessPSHeaderCell.setBorder(Rectangle.NO_BORDER);
            businessPSHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            businessPSMainTable.addCell(businessPSHeaderCell);

            document.add(businessPSMainTable);
            //End Business Quality Parameters

            document.add(blankPDFTable);
            document.add(blankPDFTable);

            //Pan India Total Rnak And Score

            //Row One Part One
            PdfPTable totalScorePDFTable = new PdfPTable(2);
            totalScorePDFTable.setWidths(new float[]{9f, 9f});
            totalScorePDFTable.setWidthPercentage(100);

            PdfPCell totalScoreTitlaCellOne = new PdfPCell(new Paragraph("Total Score", small_bold));
            totalScoreTitlaCellOne.setBackgroundColor(digParamCellOneColor);
            totalScoreTitlaCellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalScoreTitlaCellOne.setPadding(5);
            totalScorePDFTable.addCell(totalScoreTitlaCellOne);

            PdfPCell totalScoreTitleTableRowCellOne = new PdfPCell(new Paragraph(TOTAL_SCORE, small_bold_title_name));
            totalScoreTitleTableRowCellOne.setBackgroundColor(userCodeCellColor);
            totalScoreTitleTableRowCellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalScoreTitleTableRowCellOne.setPadding(5);
            totalScorePDFTable.addCell(totalScoreTitleTableRowCellOne);

            //end Row One Part One

            //Row One Part Two
            PdfPTable regionalRankPDFTable = new PdfPTable(2);
            regionalRankPDFTable.setWidths(new float[]{9f, 9f});
            regionalRankPDFTable.setWidthPercentage(100);

            PdfPCell regionalRankTitlaCellOne = new PdfPCell(new Paragraph("Regional Rank", small_bold));
            regionalRankTitlaCellOne.setBackgroundColor(digParamCellOneColor);
            regionalRankTitlaCellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
            regionalRankTitlaCellOne.setPadding(5);
            regionalRankPDFTable.addCell(regionalRankTitlaCellOne);

            PdfPCell regionalRankTitleTableRowCellOne = new PdfPCell(new Paragraph(REGIONAL_RANK, small_bold_title_name));
            regionalRankTitleTableRowCellOne.setBackgroundColor(userCodeCellColor);
            regionalRankTitleTableRowCellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
            regionalRankTitleTableRowCellOne.setPadding(5);
            regionalRankPDFTable.addCell(regionalRankTitleTableRowCellOne);
            //end Row One Part Two

            //Row One Part Three
            PdfPTable panIndiaRankPDFTable = new PdfPTable(2);
            panIndiaRankPDFTable.setWidths(new float[]{9f, 9f});
            panIndiaRankPDFTable.setWidthPercentage(100);

            PdfPCell panIndiaRankTitlaCellOne = new PdfPCell(new Paragraph("Pan India Rank", small_bold));
            panIndiaRankTitlaCellOne.setBackgroundColor(digParamCellOneColor);
            panIndiaRankTitlaCellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
            panIndiaRankTitlaCellOne.setPadding(5);
            panIndiaRankPDFTable.addCell(panIndiaRankTitlaCellOne);

            PdfPCell panIndiaRankTitleTableRowCellOne = new PdfPCell(new Paragraph(PAN_INDIA_RANK, small_bold_title_name));
            panIndiaRankTitleTableRowCellOne.setBackgroundColor(userCodeCellColor);
            panIndiaRankTitleTableRowCellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
            panIndiaRankTitleTableRowCellOne.setPadding(5);
            panIndiaRankPDFTable.addCell(panIndiaRankTitleTableRowCellOne);
            //end Row One Part Three

            //End Pan India Total Rank And Score
            //Main Table
            PdfPTable totalRankMainTable = new PdfPTable(3);
            totalRankMainTable.setSpacingBefore(2f);
            totalRankMainTable.setWidthPercentage(100);

            PdfPCell totalRankHeaderCell;

            totalRankHeaderCell = new PdfPCell(totalScorePDFTable);
            totalRankHeaderCell.setPadding(5);
            totalRankHeaderCell.setBorder(Rectangle.NO_BORDER);
            totalRankHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalRankMainTable.addCell(totalRankHeaderCell);

            totalRankHeaderCell = new PdfPCell(regionalRankPDFTable);
            totalRankHeaderCell.setPadding(5);
            totalRankHeaderCell.setBorder(Rectangle.NO_BORDER);
            totalRankHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalRankMainTable.addCell(totalRankHeaderCell);

            totalRankHeaderCell = new PdfPCell(panIndiaRankPDFTable);
            totalRankHeaderCell.setPadding(5);
            totalRankHeaderCell.setBorder(Rectangle.NO_BORDER);
            totalRankHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalRankMainTable.addCell(totalRankHeaderCell);

            document.add(totalRankMainTable);
            //End Pan India Total Rank And Score


            document.add(blankPDFTable);


            PdfPTable disclaimerPDFTable = new PdfPTable(1);
            disclaimerPDFTable.setWidthPercentage(100);

            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Disclaimer :- i - Renewal premium amount and YTD renewal budget achievement is inclusive of RRB premium .\n" +
                    "                  i i - New business premium quoted is based on net rated premium .");

            if (userType.equalsIgnoreCase("UM") || userType.equalsIgnoreCase("BDM")) {
                stringBuffer.append("\n                  i i i - NPS Score is not applicable for the scorecard.");
            }
            PdfPCell disclaimerTableCell = new PdfPCell(new Paragraph(stringBuffer.toString(), small_bold));
            disclaimerTableCell.setBackgroundColor(digParamCellOneColor);
            disclaimerTableCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            disclaimerTableCell.setPadding(5);
            disclaimerPDFTable.addCell(disclaimerTableCell);
            document.add(disclaimerPDFTable);


            document.add(blankPDFTable);
            document.add(blankPDFTable);

            PdfPTable ratingSheetPDFTable = new PdfPTable(1);
            ratingSheetPDFTable.setWidthPercentage(100);
            PdfPCell ratingSheetTableCell = new PdfPCell(new Paragraph("Rating Sheet", yellowHeaderBold));
            ratingSheetTableCell.setBackgroundColor(titlePDFTableColor);
            ratingSheetTableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            ratingSheetTableCell.setPadding(5);
            ratingSheetPDFTable.addCell(ratingSheetTableCell);
            document.add(ratingSheetPDFTable);

            document.add(blankPDFTable);

            //Digital Param Part One Page Two
            //Row One
            PdfPTable digitalParameterPageTwoTableOne = new PdfPTable(8);
            digitalParameterPageTwoTableOne.setWidths(new float[]{3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f});
            digitalParameterPageTwoTableOne.setWidthPercentage(100);

            PdfPCell digParamStaticTableOne = new PdfPCell(new Paragraph("Digital Parameters (Weightage 20)", small_bold));
            digParamStaticTableOne.setBackgroundColor(digParamCellOneColor);
            digParamStaticTableOne.setPadding(5);
            digParamStaticTableOne.setHorizontalAlignment(Element.ALIGN_CENTER);
            digParamStaticTableOne.setColspan(8);
            digitalParameterPageTwoTableOne.addCell(digParamStaticTableOne);

            PdfPCell digParamPageTwoTableCellOne = new PdfPCell(new Paragraph("Digital Adoption % (Weightage 5)", small_bold));
            digParamPageTwoTableCellOne.setBackgroundColor(digParamTableRowColor);
            digParamPageTwoTableCellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
            digParamPageTwoTableCellOne.setPadding(5);
            digParamPageTwoTableCellOne.setColspan(2);
            digitalParameterPageTwoTableOne.addCell(digParamPageTwoTableCellOne);

            PdfPCell digParamPageTwoTablCellTwo = new PdfPCell(new Paragraph("Smart Advisor Adoption %\n" +
                    "(Weightage 5)", small_bold));
            digParamPageTwoTablCellTwo.setBackgroundColor(digParamTableRowColor);
            digParamPageTwoTablCellTwo.setHorizontalAlignment(Element.ALIGN_CENTER);
            digParamPageTwoTablCellTwo.setPadding(5);
            digParamPageTwoTablCellTwo.setColspan(2);
            digitalParameterPageTwoTableOne.addCell(digParamPageTwoTablCellTwo);

            PdfPCell digParamPageTwoTableCellThree = new PdfPCell(new Paragraph("E-Shiksha Empowered –Usage % (Weightage 5)", small_bold));
            digParamPageTwoTableCellThree.setBackgroundColor(digParamTableRowColor);
            digParamPageTwoTableCellThree.setHorizontalAlignment(Element.ALIGN_CENTER);
            digParamPageTwoTableCellThree.setPadding(5);
            digParamPageTwoTableCellThree.setColspan(2);
            digitalParameterPageTwoTableOne.addCell(digParamPageTwoTableCellThree);

            PdfPCell digParamPageTwoTableCellFour = new PdfPCell(new Paragraph("Customer self Service & other's % (Weightage 5)", small_bold));
            digParamPageTwoTableCellFour.setBackgroundColor(digParamTableRowColor);
            digParamPageTwoTableCellFour.setHorizontalAlignment(Element.ALIGN_CENTER);
            digParamPageTwoTableCellFour.setPadding(5);
            digParamPageTwoTableCellFour.setColspan(2);
            digitalParameterPageTwoTableOne.addCell(digParamPageTwoTableCellFour);
            //end Row One


            //Row second
            PdfPCell digparamRowPageTwoThreeCellOne = new PdfPCell(new Paragraph("Rating", small_bold));
            digparamRowPageTwoThreeCellOne.setBackgroundColor(digParamCellOneColor);
            digparamRowPageTwoThreeCellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
            digparamRowPageTwoThreeCellOne.setPadding(5);
            //NBParamRowThreeCellOne.setColspan(2);

            PdfPCell digParamRowPageTwoThreeCellTwo = new PdfPCell(new Paragraph("Measurable", small_bold));
            digParamRowPageTwoThreeCellTwo.setBackgroundColor(digParamCellOneColor);
            digParamRowPageTwoThreeCellTwo.setHorizontalAlignment(Element.ALIGN_CENTER);
            digParamRowPageTwoThreeCellTwo.setPadding(5);
            //NBParamRowThreeCellTwo.setColspan(2);
            digitalParameterPageTwoTableOne.addCell(digparamRowPageTwoThreeCellOne);
            digitalParameterPageTwoTableOne.addCell(digParamRowPageTwoThreeCellTwo);
            digitalParameterPageTwoTableOne.addCell(digparamRowPageTwoThreeCellOne);
            digitalParameterPageTwoTableOne.addCell(digParamRowPageTwoThreeCellTwo);
            digitalParameterPageTwoTableOne.addCell(digparamRowPageTwoThreeCellOne);
            digitalParameterPageTwoTableOne.addCell(digParamRowPageTwoThreeCellTwo);
            digitalParameterPageTwoTableOne.addCell(digparamRowPageTwoThreeCellOne);
            digitalParameterPageTwoTableOne.addCell(digParamRowPageTwoThreeCellTwo);
            //End Row second

            ////////////////////////// Table one Page two

            //End Digital Param Part One Page Two


            //NB Parameter Details
            //Part Two Page 2
            PdfPTable NBParameterPageTwoTableRowTwoPartTwo = new PdfPTable(10);
            NBParameterPageTwoTableRowTwoPartTwo.setWidths(new float[]{3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f});
            NBParameterPageTwoTableRowTwoPartTwo.setWidthPercentage(100);

            PdfPCell staticTableTwo = new PdfPCell(new Paragraph("Customer Servicing Parameters (Weightage 25)", small_bold));
            staticTableTwo.setBackgroundColor(digParamCellOneColor);
            staticTableTwo.setPadding(5);
            staticTableTwo.setHorizontalAlignment(Element.ALIGN_CENTER);
            staticTableTwo.setColspan(10);
            NBParameterPageTwoTableRowTwoPartTwo.addCell(staticTableTwo);


            PdfPCell NBParamPageTwoCellOne = new PdfPCell(new Paragraph("Early Claims Count (Weightage 5)", small_bold));
            NBParamPageTwoCellOne.setBackgroundColor(busQualityParamTableRowColor);
            NBParamPageTwoCellOne.setPadding(5);
            NBParamPageTwoCellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
            NBParamPageTwoCellOne.setColspan(2);
            NBParameterPageTwoTableRowTwoPartTwo.addCell(NBParamPageTwoCellOne);


            PdfPCell NBParameterPageTwoTableCellOne = new PdfPCell(new Paragraph("Misselling Complaints Count (Weightage 5)", small_bold));
            NBParameterPageTwoTableCellOne.setBackgroundColor(busQualityParamTableRowColor);
            NBParameterPageTwoTableCellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
            NBParameterPageTwoTableCellOne.setPadding(5);
            NBParameterPageTwoTableCellOne.setColspan(2);
            NBParameterPageTwoTableRowTwoPartTwo.addCell(NBParameterPageTwoTableCellOne);

            PdfPCell NBParameterPageTwoTableCellTwo = new PdfPCell(new Paragraph("FLC Vs Issuance % (Weightage 5", small_bold));
            NBParameterPageTwoTableCellTwo.setBackgroundColor(busQualityParamTableRowColor);
            NBParameterPageTwoTableCellTwo.setHorizontalAlignment(Element.ALIGN_CENTER);
            NBParameterPageTwoTableCellTwo.setPadding(5);
            NBParameterPageTwoTableCellTwo.setColspan(2);
            NBParameterPageTwoTableRowTwoPartTwo.addCell(NBParameterPageTwoTableCellTwo);


            PdfPCell NBParameterPageTwoTableCellFour = new PdfPCell(new Paragraph("PNR Complaints(Count) (Weightage 5)", small_bold));
            NBParameterPageTwoTableCellFour.setBackgroundColor(busQualityParamTableRowColor);
            NBParameterPageTwoTableCellFour.setHorizontalAlignment(Element.ALIGN_CENTER);
            NBParameterPageTwoTableCellFour.setPadding(5);
            NBParameterPageTwoTableCellFour.setColspan(2);
            NBParameterPageTwoTableRowTwoPartTwo.addCell(NBParameterPageTwoTableCellFour);

            PdfPCell staticTableTwoRowTwoCellFourMain = new PdfPCell(new Paragraph("NPS Score (Weightage 5)", small_bold));
            staticTableTwoRowTwoCellFourMain.setBackgroundColor(busQualityParamTableRowColor);
            staticTableTwoRowTwoCellFourMain.setHorizontalAlignment(Element.ALIGN_CENTER);
            staticTableTwoRowTwoCellFourMain.setPadding(5);
            staticTableTwoRowTwoCellFourMain.setColspan(2);
            NBParameterPageTwoTableRowTwoPartTwo.addCell(staticTableTwoRowTwoCellFourMain);

            //Second Row
            PdfPCell NBParamPageTwoRowThreeCellOne = new PdfPCell(new Paragraph("Rating", small_bold));
            NBParamPageTwoRowThreeCellOne.setBackgroundColor(digParamCellOneColor);
            NBParamPageTwoRowThreeCellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
            NBParamPageTwoRowThreeCellOne.setPadding(5);
            //NBParamRowThreeCellOne.setColspan(2);

            PdfPCell NBParamPageTwoRowThreeCellTwo = new PdfPCell(new Paragraph("Measurable", small_bold));
            NBParamPageTwoRowThreeCellTwo.setBackgroundColor(digParamCellOneColor);
            NBParamPageTwoRowThreeCellTwo.setHorizontalAlignment(Element.ALIGN_CENTER);
            NBParamPageTwoRowThreeCellTwo.setPadding(5);
            //NBParamRowThreeCellTwo.setColspan(2);
            NBParameterPageTwoTableRowTwoPartTwo.addCell(NBParamPageTwoRowThreeCellOne);
            NBParameterPageTwoTableRowTwoPartTwo.addCell(NBParamPageTwoRowThreeCellTwo);

            NBParameterPageTwoTableRowTwoPartTwo.addCell(NBParamPageTwoRowThreeCellOne);
            NBParameterPageTwoTableRowTwoPartTwo.addCell(NBParamPageTwoRowThreeCellTwo);

            NBParameterPageTwoTableRowTwoPartTwo.addCell(NBParamPageTwoRowThreeCellOne);
            NBParameterPageTwoTableRowTwoPartTwo.addCell(NBParamPageTwoRowThreeCellTwo);

            NBParameterPageTwoTableRowTwoPartTwo.addCell(NBParamPageTwoRowThreeCellOne);
            NBParameterPageTwoTableRowTwoPartTwo.addCell(NBParamPageTwoRowThreeCellTwo);

            NBParameterPageTwoTableRowTwoPartTwo.addCell(NBParamPageTwoRowThreeCellOne);
            NBParameterPageTwoTableRowTwoPartTwo.addCell(NBParamPageTwoRowThreeCellTwo);
            //end Second Row

            ////////////////////table Two Page two

            //end Part Two Page 2
            //End NB Parameter Details


            //Business Quality Parameters Page Two
            //Row One Part One
            //Row One
            PdfPTable busQualityParameterPageTwoTableRowOne = new PdfPTable(12);
            busQualityParameterPageTwoTableRowOne.setWidths(new float[]{1f, 3f, 1f, 3f, 1f, 3f, 1f, 3f, 1f, 3f, 1f, 3f});
            busQualityParameterPageTwoTableRowOne.setWidthPercentage(100);

            PdfPCell staticTableThree = new PdfPCell(new Paragraph("New Business Parameters (Weightage 35)", small_bold));
            staticTableThree.setBackgroundColor(digParamCellOneColor);
            staticTableThree.setPadding(5);
            staticTableThree.setHorizontalAlignment(Element.ALIGN_CENTER);
            staticTableThree.setColspan(12);
            busQualityParameterPageTwoTableRowOne.addCell(staticTableThree);

            PdfPCell busQualityParamPageTwoTableCellOne = new PdfPCell(new Paragraph("FTR Ratio % (Weightage 10)", small_bold));
            busQualityParamPageTwoTableCellOne.setBackgroundColor(NBParameterTableRowColor);
            busQualityParamPageTwoTableCellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
            busQualityParamPageTwoTableCellOne.setPadding(5);
            busQualityParamPageTwoTableCellOne.setColspan(2);
            busQualityParameterPageTwoTableRowOne.addCell(busQualityParamPageTwoTableCellOne);

            PdfPCell busQualityParamPageTwoTableCellTwo = new PdfPCell(new Paragraph("NB Refund Ratio % (Weightage 5)", small_bold));
            busQualityParamPageTwoTableCellTwo.setBackgroundColor(NBParameterTableRowColor);
            busQualityParamPageTwoTableCellTwo.setHorizontalAlignment(Element.ALIGN_CENTER);
            busQualityParamPageTwoTableCellTwo.setPadding(5);
            busQualityParamPageTwoTableCellTwo.setColspan(2);
            busQualityParameterPageTwoTableRowOne.addCell(busQualityParamPageTwoTableCellTwo);

            PdfPCell busQualityParamPageTwoTableCellThree = new PdfPCell(new Paragraph(tableThreePageOneMainCellThree, small_bold));
            busQualityParamPageTwoTableCellThree.setBackgroundColor(NBParameterTableRowColor);
            busQualityParamPageTwoTableCellThree.setHorizontalAlignment(Element.ALIGN_CENTER);
            busQualityParamPageTwoTableCellThree.setPadding(5);
            busQualityParamPageTwoTableCellThree.setColspan(2);
            busQualityParameterPageTwoTableRowOne.addCell(busQualityParamPageTwoTableCellThree);

            PdfPCell busQualityParamPageTwoTableCellFour = new PdfPCell(new Paragraph("Requirement Closure TAT (Average days) (Weightage 5)", small_bold));
            busQualityParamPageTwoTableCellFour.setBackgroundColor(NBParameterTableRowColor);
            busQualityParamPageTwoTableCellFour.setHorizontalAlignment(Element.ALIGN_CENTER);
            busQualityParamPageTwoTableCellFour.setPadding(5);
            busQualityParamPageTwoTableCellFour.setColspan(2);
            busQualityParameterPageTwoTableRowOne.addCell(busQualityParamPageTwoTableCellFour);

            PdfPCell busQualityParamPageTwoTableCellfive = new PdfPCell(new Paragraph("PIWC Mismatch (Count)(Weightage 5)", small_bold));
            busQualityParamPageTwoTableCellfive.setBackgroundColor(NBParameterTableRowColor);
            busQualityParamPageTwoTableCellfive.setHorizontalAlignment(Element.ALIGN_CENTER);
            busQualityParamPageTwoTableCellfive.setPadding(5);
            busQualityParamPageTwoTableCellfive.setColspan(2);
            busQualityParameterPageTwoTableRowOne.addCell(busQualityParamPageTwoTableCellfive);

            PdfPCell busQualityParamPageTwoTableCellSix = new PdfPCell(new Paragraph("PIWC Clearance Efficiency (Weightage 5)", small_bold));
            busQualityParamPageTwoTableCellSix.setBackgroundColor(NBParameterTableRowColor);
            busQualityParamPageTwoTableCellSix.setHorizontalAlignment(Element.ALIGN_CENTER);
            busQualityParamPageTwoTableCellSix.setPadding(5);
            busQualityParamPageTwoTableCellSix.setColspan(2);
            busQualityParameterPageTwoTableRowOne.addCell(busQualityParamPageTwoTableCellSix);

            //end Row One


            //Second Row
            PdfPCell busQualityparamPageTwoRowThreeCellOne = new PdfPCell(new Paragraph("Rating", small_bold));
            busQualityparamPageTwoRowThreeCellOne.setBackgroundColor(digParamCellOneColor);
            busQualityparamPageTwoRowThreeCellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
            busQualityparamPageTwoRowThreeCellOne.setPadding(5);

            PdfPCell busQualityParamPageTwoRowThreeCellTwo = new PdfPCell(new Paragraph("Measurable", small_bold1));
            busQualityParamPageTwoRowThreeCellTwo.setBackgroundColor(digParamCellOneColor);
            busQualityParamPageTwoRowThreeCellTwo.setHorizontalAlignment(Element.ALIGN_CENTER);
            busQualityParamPageTwoRowThreeCellTwo.setPadding(5);

            busQualityParameterPageTwoTableRowOne.addCell(busQualityparamPageTwoRowThreeCellOne);
            busQualityParameterPageTwoTableRowOne.addCell(busQualityParamPageTwoRowThreeCellTwo);
            busQualityParameterPageTwoTableRowOne.addCell(busQualityparamPageTwoRowThreeCellOne);
            busQualityParameterPageTwoTableRowOne.addCell(busQualityParamPageTwoRowThreeCellTwo);
            busQualityParameterPageTwoTableRowOne.addCell(busQualityparamPageTwoRowThreeCellOne);
            busQualityParameterPageTwoTableRowOne.addCell(busQualityParamPageTwoRowThreeCellTwo);

            busQualityParameterPageTwoTableRowOne.addCell(busQualityparamPageTwoRowThreeCellOne);
            busQualityParameterPageTwoTableRowOne.addCell(busQualityParamPageTwoRowThreeCellTwo);
            busQualityParameterPageTwoTableRowOne.addCell(busQualityparamPageTwoRowThreeCellOne);
            busQualityParameterPageTwoTableRowOne.addCell(busQualityParamPageTwoRowThreeCellTwo);
            busQualityParameterPageTwoTableRowOne.addCell(busQualityparamPageTwoRowThreeCellOne);
            busQualityParameterPageTwoTableRowOne.addCell(busQualityParamPageTwoRowThreeCellTwo);
            //End Second Row

            /////////Table Three Page Two

            //End Row One part 1

            //PS Parameters Details
            //Row One part 2
            PdfPTable PSParameterPageTwoTableTwoPartTwo = new PdfPTable(8);
            PSParameterPageTwoTableTwoPartTwo.setWidths(new float[]{3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f});
            PSParameterPageTwoTableTwoPartTwo.setWidthPercentage(100);

            PdfPCell staticTableFour = new PdfPCell(new Paragraph("Persistency Parameters (Weightage 20)", small_bold));
            staticTableFour.setBackgroundColor(digParamCellOneColor);
            staticTableFour.setPadding(5);
            staticTableFour.setHorizontalAlignment(Element.ALIGN_CENTER);
            staticTableFour.setColspan(12);
            PSParameterPageTwoTableTwoPartTwo.addCell(staticTableFour);

            PdfPCell PSParameterPageTwoTableCellOne = new PdfPCell(new Paragraph("13th Month Persistency (Weightage 5)", small_bold));
            PSParameterPageTwoTableCellOne.setBackgroundColor(PSParameterTableRowColor);
            PSParameterPageTwoTableCellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
            PSParameterPageTwoTableCellOne.setPadding(5);
            PSParameterPageTwoTableCellOne.setColspan(2);
            PSParameterPageTwoTableTwoPartTwo.addCell(PSParameterPageTwoTableCellOne);

            PdfPCell PSParameterPageTwoTableCellTwo = new PdfPCell(new Paragraph("61st Month Persistency (Weightage 5)", small_bold));
            PSParameterPageTwoTableCellTwo.setBackgroundColor(PSParameterTableRowColor);
            PSParameterPageTwoTableCellTwo.setHorizontalAlignment(Element.ALIGN_CENTER);
            PSParameterPageTwoTableCellTwo.setPadding(5);
            PSParameterPageTwoTableCellTwo.setColspan(2);
            PSParameterPageTwoTableTwoPartTwo.addCell(PSParameterPageTwoTableCellTwo);

            PdfPCell PSParameterPageTwoTableCellThree = new PdfPCell(new Paragraph("YTD Renewal Budget Achievement (Weightage 5)", small_bold));
            PSParameterPageTwoTableCellThree.setBackgroundColor(PSParameterTableRowColor);
            PSParameterPageTwoTableCellThree.setHorizontalAlignment(Element.ALIGN_CENTER);
            PSParameterPageTwoTableCellThree.setPadding(5);
            PSParameterPageTwoTableCellThree.setColspan(2);
            PSParameterPageTwoTableTwoPartTwo.addCell(PSParameterPageTwoTableCellThree);

            PdfPCell PSParameterPageTwoTableCellFour = new PdfPCell(new Paragraph("Alternate Mode Registration (Weightage 5)", small_bold));
            PSParameterPageTwoTableCellFour.setBackgroundColor(PSParameterTableRowColor);
            PSParameterPageTwoTableCellFour.setHorizontalAlignment(Element.ALIGN_CENTER);
            PSParameterPageTwoTableCellFour.setPadding(5);
            PSParameterPageTwoTableCellFour.setColspan(2);
            PSParameterPageTwoTableTwoPartTwo.addCell(PSParameterPageTwoTableCellFour);


            //Second Row
            PdfPCell PSParamRowPageTwoThreeCellOne = new PdfPCell(new Paragraph("Rating", small_bold));
            PSParamRowPageTwoThreeCellOne.setBackgroundColor(digParamCellOneColor);
            PSParamRowPageTwoThreeCellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
            PSParamRowPageTwoThreeCellOne.setPadding(5);
            //NBParamRowThreeCellOne.setColspan(2);

            PdfPCell PSParamRowPageTwoThreeCellTwo = new PdfPCell(new Paragraph("Measurable", small_bold));
            PSParamRowPageTwoThreeCellTwo.setBackgroundColor(digParamCellOneColor);
            PSParamRowPageTwoThreeCellTwo.setHorizontalAlignment(Element.ALIGN_CENTER);
            PSParamRowPageTwoThreeCellTwo.setPadding(5);
            //NBParamRowThreeCellTwo.setColspan(2);
            PSParameterPageTwoTableTwoPartTwo.addCell(PSParamRowPageTwoThreeCellOne);
            PSParameterPageTwoTableTwoPartTwo.addCell(PSParamRowPageTwoThreeCellTwo);
            PSParameterPageTwoTableTwoPartTwo.addCell(PSParamRowPageTwoThreeCellOne);
            PSParameterPageTwoTableTwoPartTwo.addCell(PSParamRowPageTwoThreeCellTwo);
            PSParameterPageTwoTableTwoPartTwo.addCell(PSParamRowPageTwoThreeCellOne);
            PSParameterPageTwoTableTwoPartTwo.addCell(PSParamRowPageTwoThreeCellTwo);
            PSParameterPageTwoTableTwoPartTwo.addCell(PSParamRowPageTwoThreeCellOne);
            PSParameterPageTwoTableTwoPartTwo.addCell(PSParamRowPageTwoThreeCellTwo);
            //end Second Row
            //end Row One Part 2

            for (int i = 0; i < 6; i++) {

                String tableOneCellTwo = "", tableOneCellFour = "", tableOneCellSix = "", tableOneCellEight = "",
                        tableTwoCellTwo = "", tableTwoCellFour = "", tableTwoCellSix = "", tableTwoCellEight = "", tableTwoCellTen = "",
                        tableThreeCellTwo = "", tableThreeCellFour = "", tableThreeCellSix = "", tableThreeCellEight = "", tableThreeCellten = "", tableThreeCellTwelve = "",
                        tableFourCellTwo = "", tableFourCellFour = "", tableFourCellSix = "", tableFourCellEight = "";
                if (userType.equalsIgnoreCase("BSM")
                        || userType.equalsIgnoreCase("DSM")
                        || userType.equalsIgnoreCase("ASM")
                        || userType.equalsIgnoreCase("RSM")) {
                    if (i == 0) {
                        tableOneCellTwo = "<60 %";
                        tableOneCellFour = "<60 %";
                        tableOneCellSix = "<50%";
                        tableOneCellEight = "<3%";

                        tableTwoCellTwo = ">= 750";
                        tableTwoCellFour = ">= 130";
                        tableTwoCellSix = "3%";
                        tableTwoCellEight = ">=110";
                        tableTwoCellTen = "<12";

                        tableThreeCellTwo = "<50 %";
                        tableThreeCellFour = ">6%";
                        tableThreeCellSix = "<50 %";
                        tableThreeCellEight = "Regional Avg-PAN India Avg > 3";
                        tableThreeCellten = ">= 3200";
                        tableThreeCellTwelve = "<88%";

                        tableFourCellTwo = "<80 %";
                        tableFourCellFour = "<55 %";
                        tableFourCellSix = "<95 %";
                        tableFourCellEight = "<50 %";
                    } else if (i == 1) {
                        tableOneCellTwo = ">=60%";
                        tableOneCellFour = ">=60%";
                        tableOneCellSix = ">=50%";
                        tableOneCellEight = ">=3%";

                        tableTwoCellTwo = ">=600&<750";
                        tableTwoCellFour = ">=100&<130";
                        tableTwoCellSix = ">2%";
                        tableTwoCellEight = ">=90&<110";
                        tableTwoCellTen = "12 to 15";

                        tableThreeCellTwo = ">=50%";
                        tableThreeCellFour = "<=6%";
                        tableThreeCellSix = ">=50%";
                        tableThreeCellEight = "Regional Avg-PAN India Avg = 3";
                        tableThreeCellten = ">=2700&<3200";
                        tableThreeCellTwelve = "<88%";

                        tableFourCellTwo = "80%";
                        tableFourCellFour = "55%";
                        tableFourCellSix = "95%";
                        tableFourCellEight = "50%";
                    } else if (i == 2) {
                        tableOneCellTwo = ">=88%";
                        tableOneCellFour = ">=70%";
                        tableOneCellSix = ">=60%";
                        tableOneCellEight = ">5%";

                        tableTwoCellTwo = ">=500&<600";
                        tableTwoCellFour = ">=70&<100";
                        tableTwoCellSix = ">1%";
                        tableTwoCellEight = ">=65&< 90";
                        tableTwoCellTen = "16 to 25";

                        tableThreeCellTwo = ">=55%";
                        tableThreeCellFour = "<=5%";
                        tableThreeCellSix = ">=70%";
                        tableThreeCellEight = "Regional Avg-PAN India Avg = 2";
                        tableThreeCellten = ">=2100&<2700";
                        tableThreeCellTwelve = "90%";

                        tableFourCellTwo = "82%";
                        tableFourCellFour = "56%";
                        tableFourCellSix = "97%";
                        tableFourCellEight = "56%";
                    } else if (i == 3) {
                        tableOneCellTwo = ">=92%";
                        tableOneCellFour = ">=80%";
                        tableOneCellSix = ">=70%";
                        tableOneCellEight = ">6%";

                        tableTwoCellTwo = ">=350&<500";
                        tableTwoCellFour = ">=35&<70";
                        tableTwoCellSix = ">0.50%";
                        tableTwoCellEight = ">=45&<65";
                        tableTwoCellTen = "26 to 30";

                        tableThreeCellTwo = ">=60%";
                        tableThreeCellFour = "<=4%";
                        tableThreeCellSix = ">=80%";
                        tableThreeCellEight = "Regional Avg = Pan India Avg";
                        tableThreeCellten = ">=1500&<2100";
                        tableThreeCellTwelve = "92%";

                        tableFourCellTwo = "84%";
                        tableFourCellFour = "58%";
                        tableFourCellSix = "98%";
                        tableFourCellEight = "61%";
                    } else if (i == 4) {

                        tableOneCellTwo = ">=95%";
                        tableOneCellFour = ">=90%";
                        tableOneCellSix = ">=75%";
                        tableOneCellEight = ">7%";

                        tableTwoCellTwo = ">= 250 and <350";
                        tableTwoCellFour = ">=10&<35";
                        tableTwoCellSix = ">0.01%";
                        tableTwoCellEight = ">=25&<45";
                        tableTwoCellTen = "31 to 35";

                        tableThreeCellTwo = ">=65%";
                        tableThreeCellFour = "<=2%";
                        tableThreeCellSix = ">=90%";
                        tableThreeCellEight = "Regional Avg-PAN India Avg = -2";
                        tableThreeCellten = ">=900<1500";
                        tableThreeCellTwelve = "95%";

                        tableFourCellTwo = "86%";
                        tableFourCellFour = "60%";
                        tableFourCellSix = "100%";
                        tableFourCellEight = "70%";
                    } else if (i == 5) {
                        tableOneCellTwo = ">=98%";
                        tableOneCellFour = ">=95%";
                        tableOneCellSix = ">=85%";
                        tableOneCellEight = ">=10%";

                        tableTwoCellTwo = "<250";
                        tableTwoCellFour = "< 10";
                        tableTwoCellSix = "0%";
                        tableTwoCellEight = "< 25";
                        tableTwoCellTen = "Above 35";

                        tableThreeCellTwo = ">=70%";
                        tableThreeCellFour = "=0%";
                        tableThreeCellSix = ">=95%";
                        tableThreeCellEight = "Regional Avg-PAN India Avg > -2";
                        tableThreeCellten = "< 900";
                        tableThreeCellTwelve = ">97%";

                        tableFourCellTwo = ">87%";
                        tableFourCellFour = ">61%";
                        tableFourCellSix = "102%";
                        tableFourCellEight = ">75%";
                    }

                } else {

                    if (userType.equalsIgnoreCase("BDM") || userType.equalsIgnoreCase("UM")) {
                        if (i == 0) {
                            tableOneCellTwo = "<60 %";
                            tableOneCellFour = "<60 %";
                            tableOneCellSix = "<50%";
                            tableOneCellEight = "<3%";

                            tableTwoCellTwo = ">4";
                            tableTwoCellFour = ">4";
                            tableTwoCellSix = "3%";
                            tableTwoCellEight = ">4";
                            tableTwoCellTen = "<12";

                            tableThreeCellTwo = "<50 %";
                            tableThreeCellFour = ">6%";
                            if (userType.equalsIgnoreCase("UM")) {
                                tableThreeCellSix = "<50%";
                            } else {
                                tableThreeCellSix = ">18%";
                            }
                            tableThreeCellEight = "PAN India Avg > 3";
                            tableThreeCellten = ">20";
                            tableThreeCellTwelve = "<88%";

                            tableFourCellTwo = "<80 %";
                            tableFourCellFour = "<55 %";
                            tableFourCellSix = "<95 %";
                            tableFourCellEight = "<50 %";
                        } else if (i == 1) {
                            tableOneCellTwo = ">=60%";
                            tableOneCellFour = ">=60%";
                            tableOneCellSix = ">=50%";
                            tableOneCellEight = ">=3%";

                            tableTwoCellTwo = "4";
                            tableTwoCellFour = "4";
                            tableTwoCellSix = ">2%";
                            tableTwoCellEight = "4";
                            tableTwoCellTen = "12 to 15";

                            tableThreeCellTwo = ">=50%";
                            tableThreeCellFour = "<=6%";
                            if (userType.equalsIgnoreCase("UM")) {
                                tableThreeCellSix = ">=50%";
                            } else {
                                tableThreeCellSix = "<=17%";
                            }
                            tableThreeCellEight = "PAN India Avg = 3";
                            tableThreeCellten = "20";
                            tableThreeCellTwelve = "88%";

                            tableFourCellTwo = "80%";
                            tableFourCellFour = "55%";
                            tableFourCellSix = "95%";
                            tableFourCellEight = "50%";
                        } else if (i == 2) {
                            tableOneCellTwo = ">=88%";
                            tableOneCellFour = ">=70%";
                            tableOneCellSix = ">=60%";
                            tableOneCellEight = ">5%";

                            tableTwoCellTwo = "3";
                            tableTwoCellFour = "3";
                            tableTwoCellSix = ">1%";
                            tableTwoCellEight = "3";
                            tableTwoCellTen = "16 to 25";

                            tableThreeCellTwo = ">=55%";
                            tableThreeCellFour = "<=5%";
                            if (userType.equalsIgnoreCase("UM")) {
                                tableThreeCellSix = ">=70%";
                            } else {
                                tableThreeCellSix = "<=16%";
                            }
                            tableThreeCellEight = "PAN India Avg = 2";
                            tableThreeCellten = "<15";
                            tableThreeCellTwelve = "90%";

                            tableFourCellTwo = "82%";
                            tableFourCellFour = "56%";
                            tableFourCellSix = "97%";
                            tableFourCellEight = "56%";
                        } else if (i == 3) {
                            tableOneCellTwo = ">=92%";
                            tableOneCellFour = ">=80%";
                            tableOneCellSix = ">=70%";
                            tableOneCellEight = ">6%";

                            tableTwoCellTwo = "2";
                            tableTwoCellFour = "2";
                            tableTwoCellSix = ">0.50%";
                            tableTwoCellEight = "2";
                            tableTwoCellTen = "26 to 30";

                            tableThreeCellTwo = ">=60%";
                            tableThreeCellFour = "<=4%";
                            if (userType.equalsIgnoreCase("UM")) {
                                tableThreeCellSix = ">=80%";
                            } else {
                                tableThreeCellSix = "<=12%";
                            }
                            tableThreeCellEight = "Pan India Avg";
                            tableThreeCellten = "<10";
                            tableThreeCellTwelve = "92%";

                            tableFourCellTwo = "84%";
                            tableFourCellFour = "58%";
                            tableFourCellSix = "98%";
                            tableFourCellEight = "61%";
                        } else if (i == 4) {

                            tableOneCellTwo = ">=95%";
                            tableOneCellFour = ">=90%";
                            tableOneCellSix = ">=75%";
                            tableOneCellEight = ">7%";

                            tableTwoCellTwo = "1";
                            tableTwoCellFour = "1";
                            tableTwoCellSix = ">0.01%";
                            tableTwoCellEight = "1";
                            tableTwoCellTen = "31 to 35";

                            tableThreeCellTwo = ">=65%";
                            tableThreeCellFour = "<=2%";

                            if (userType.equalsIgnoreCase("UM")) {
                                tableThreeCellSix = ">=90%";
                            } else {
                                tableThreeCellSix = "<=10%";
                            }
                            tableThreeCellEight = "PAN India Avg = -2";
                            tableThreeCellten = "<5";
                            tableThreeCellTwelve = "95%";

                            tableFourCellTwo = "86%";
                            tableFourCellFour = "60%";
                            tableFourCellSix = "100%";
                            tableFourCellEight = "70%";
                        } else if (i == 5) {
                            tableOneCellTwo = ">=98%";
                            tableOneCellFour = ">=95%";
                            tableOneCellSix = ">=85%";
                            tableOneCellEight = ">=10%";

                            tableTwoCellTwo = "=0";
                            tableTwoCellFour = "=0";
                            tableTwoCellSix = "0%";
                            tableTwoCellEight = "0";
                            tableTwoCellTen = "Above 35";

                            tableThreeCellTwo = ">=70%";
                            tableThreeCellFour = "=0%";
                            if (userType.equalsIgnoreCase("UM")) {
                                tableThreeCellSix = ">=95%";
                            } else {
                                tableThreeCellSix = "<=8%";
                            }
                            tableThreeCellEight = "PAN India Avg > -2";
                            tableThreeCellten = "=0";
                            tableThreeCellTwelve = ">97%";

                            tableFourCellTwo = ">87%";
                            tableFourCellFour = ">61%";
                            tableFourCellSix = "102%";
                            tableFourCellEight = ">75%";
                        }
                    } else {

                        if (i == 0) {
                            tableOneCellTwo = "<60 %";
                            tableOneCellFour = "<60 %";
                            tableOneCellSix = "<50%";
                            tableOneCellEight = "<3%";

                            tableTwoCellTwo = ">= 750";
                            tableTwoCellFour = ">= 130";
                            tableTwoCellSix = "3%";
                            tableTwoCellEight = ">=110";
                            tableTwoCellTen = "<12";

                            tableThreeCellTwo = "<50 %";
                            tableThreeCellFour = ">6%";
                            tableThreeCellSix = ">18%";
                            tableThreeCellEight = "Regional Avg-PAN India Avg > 3";
                            tableThreeCellten = ">= 3200";
                            tableThreeCellTwelve = "<88%";

                            tableFourCellTwo = "<80 %";
                            tableFourCellFour = "<55 %";
                            tableFourCellSix = "<95 %";
                            tableFourCellEight = "<50 %";
                        } else if (i == 1) {
                            tableOneCellTwo = ">=60%";
                            tableOneCellFour = ">=60%";
                            tableOneCellSix = ">=50%";
                            tableOneCellEight = ">=3%";

                            tableTwoCellTwo = ">=600&<750";
                            tableTwoCellFour = ">=100&<130";
                            tableTwoCellSix = ">2%";
                            tableTwoCellEight = ">=90&<110";
                            tableTwoCellTen = "12 to 15";

                            tableThreeCellTwo = ">=50%";
                            tableThreeCellFour = "<=6%";
                            tableThreeCellSix = "<=17%";
                            tableThreeCellEight = "Regional Avg-PAN India Avg = 3";
                            tableThreeCellten = ">=2700&<3200";
                            tableThreeCellTwelve = "<88%";

                            tableFourCellTwo = "80%";
                            tableFourCellFour = "55%";
                            tableFourCellSix = "95%";
                            tableFourCellEight = "50%";
                        } else if (i == 2) {
                            tableOneCellTwo = ">=88%";
                            tableOneCellFour = ">=70%";
                            tableOneCellSix = ">=60%";
                            tableOneCellEight = ">5%";

                            tableTwoCellTwo = ">=500&<600";
                            tableTwoCellFour = ">=70&<100";
                            tableTwoCellSix = ">1%";
                            tableTwoCellEight = ">=65&< 90";
                            tableTwoCellTen = "16 to 25";

                            tableThreeCellTwo = ">=55%";
                            tableThreeCellFour = "<=5%";
                            tableThreeCellSix = "<=16%";
                            tableThreeCellEight = "Regional Avg-PAN India Avg = 2";
                            tableThreeCellten = ">=2100&<2700";
                            tableThreeCellTwelve = "90%";

                            tableFourCellTwo = "82%";
                            tableFourCellFour = "56%";
                            tableFourCellSix = "97%";
                            tableFourCellEight = "56%";
                        } else if (i == 3) {
                            tableOneCellTwo = ">=92%";
                            tableOneCellFour = ">=80%";
                            tableOneCellSix = ">=70%";
                            tableOneCellEight = ">6%";

                            tableTwoCellTwo = ">=350&<500";
                            tableTwoCellFour = ">=35&<70";
                            tableTwoCellSix = ">0.50%";
                            tableTwoCellEight = ">=45&<65";
                            tableTwoCellTen = "26 to 30";

                            tableThreeCellTwo = ">=60%";
                            tableThreeCellFour = "<=4%";
                            tableThreeCellSix = "<=12%";
                            tableThreeCellEight = "Regional Avg = Pan India Avg";
                            tableThreeCellten = ">=1500&<2100";
                            tableThreeCellTwelve = "92%";

                            tableFourCellTwo = "84%";
                            tableFourCellFour = "58%";
                            tableFourCellSix = "98%";
                            tableFourCellEight = "61%";
                        } else if (i == 4) {

                            tableOneCellTwo = ">=95%";
                            tableOneCellFour = ">=90%";
                            tableOneCellSix = ">=75%";
                            tableOneCellEight = ">7%";

                            tableTwoCellTwo = ">= 250 and <350";
                            tableTwoCellFour = ">=10&<35";
                            tableTwoCellSix = ">0.01%";
                            tableTwoCellEight = ">=25&<45";
                            tableTwoCellTen = "31 to 35";

                            tableThreeCellTwo = ">=65%";
                            tableThreeCellFour = "<=2%";
                            tableThreeCellSix = "<=10%";
                            tableThreeCellEight = "Regional Avg-PAN India Avg = -2";
                            tableThreeCellten = ">=900<1500";
                            tableThreeCellTwelve = "95%";

                            tableFourCellTwo = "86%";
                            tableFourCellFour = "60%";
                            tableFourCellSix = "100%";
                            tableFourCellEight = "70%";
                        } else if (i == 5) {
                            tableOneCellTwo = ">=98%";
                            tableOneCellFour = ">=95%";
                            tableOneCellSix = ">=85%";
                            tableOneCellEight = ">=10%";

                            tableTwoCellTwo = "<250";
                            tableTwoCellFour = "< 10";
                            tableTwoCellSix = "0%";
                            tableTwoCellEight = "< 25";
                            tableTwoCellTen = "Above 35";

                            tableThreeCellTwo = ">=70%";
                            tableThreeCellFour = "=0%";
                            tableThreeCellSix = "<=8%";
                            tableThreeCellEight = "Regional Avg-PAN India Avg > -2";
                            tableThreeCellten = "< 900";
                            tableThreeCellTwelve = ">97%";

                            tableFourCellTwo = ">87%";
                            tableFourCellFour = ">61%";
                            tableFourCellSix = "102%";
                            tableFourCellEight = ">75%";
                        }

                    }
                }

                //Static Row
                PdfPCell tableOneRowThreeCellOneValue = new PdfPCell(new Paragraph(i + "", small_bold));
                tableOneRowThreeCellOneValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableOneRowThreeCellOneValue.setPadding(5);
                //NBParamRowThreeCellOne.setColspan(2);
                digitalParameterPageTwoTableOne.addCell(tableOneRowThreeCellOneValue);


                PdfPCell tableOneRowThreeCellTwoValue = new PdfPCell(new Paragraph(tableOneCellTwo, small_bold));
                tableOneRowThreeCellTwoValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableOneRowThreeCellTwoValue.setPadding(5);
                digitalParameterPageTwoTableOne.addCell(tableOneRowThreeCellTwoValue);

                PdfPCell tableOneRowSevenCellThreeValue = new PdfPCell(new Paragraph(i + "", small_bold));
                tableOneRowSevenCellThreeValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableOneRowSevenCellThreeValue.setPadding(5);
                digitalParameterPageTwoTableOne.addCell(tableOneRowSevenCellThreeValue);

                PdfPCell tableOneRowSevenCellFourValue = new PdfPCell(new Paragraph(tableOneCellFour, small_bold));
                tableOneRowSevenCellFourValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableOneRowSevenCellFourValue.setPadding(5);
                digitalParameterPageTwoTableOne.addCell(tableOneRowSevenCellFourValue);

                PdfPCell tableOneRowSevenCellFIveValue = new PdfPCell(new Paragraph(i + "", small_bold));
                tableOneRowSevenCellFIveValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableOneRowSevenCellFIveValue.setPadding(5);
                digitalParameterPageTwoTableOne.addCell(tableOneRowSevenCellFIveValue);

                PdfPCell tableOneRowSevenCellSixValue = new PdfPCell(new Paragraph(tableOneCellSix, small_bold));
                tableOneRowSevenCellSixValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableOneRowSevenCellSixValue.setPadding(5);
                digitalParameterPageTwoTableOne.addCell(tableOneRowSevenCellSixValue);

                PdfPCell tableOneRowSevenCellSevenValue = new PdfPCell(new Paragraph(i + "", small_bold));
                tableOneRowSevenCellSevenValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableOneRowSevenCellSevenValue.setPadding(5);
                digitalParameterPageTwoTableOne.addCell(tableOneRowSevenCellSevenValue);

                PdfPCell tableOneRowThreeCellEightValue = new PdfPCell(new Paragraph(tableOneCellEight, small_bold));
                tableOneRowThreeCellEightValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableOneRowThreeCellEightValue.setPadding(5);
                digitalParameterPageTwoTableOne.addCell(tableOneRowThreeCellEightValue);

                //End Static Table One Page Two

                //Static Table Two Page Two
                PdfPCell tableTwoRowThreeCellOneValue = new PdfPCell(new Paragraph(i + "", small_bold));
                tableTwoRowThreeCellOneValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableTwoRowThreeCellOneValue.setPadding(5);
                NBParameterPageTwoTableRowTwoPartTwo.addCell(tableTwoRowThreeCellOneValue);


                PdfPCell tableTwoRowThreeCellTwoValue = new PdfPCell(new Paragraph(tableTwoCellTwo, small_bold));
                tableTwoRowThreeCellTwoValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableTwoRowThreeCellTwoValue.setPadding(5);
                NBParameterPageTwoTableRowTwoPartTwo.addCell(tableTwoRowThreeCellTwoValue);

                PdfPCell tableTwoRowSevenCellThreeValue = new PdfPCell(new Paragraph(i + "", small_bold));
                tableTwoRowSevenCellThreeValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableTwoRowSevenCellThreeValue.setPadding(5);
                NBParameterPageTwoTableRowTwoPartTwo.addCell(tableTwoRowSevenCellThreeValue);

                PdfPCell tableTwoRowSevenCellFourValue = new PdfPCell(new Paragraph(tableTwoCellFour, small_bold));
                tableTwoRowSevenCellFourValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableTwoRowSevenCellFourValue.setPadding(5);
                NBParameterPageTwoTableRowTwoPartTwo.addCell(tableTwoRowSevenCellFourValue);

                PdfPCell tableTwoRowSevenCellFIveValue = new PdfPCell(new Paragraph(i + "", small_bold));
                tableTwoRowSevenCellFIveValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableTwoRowSevenCellFIveValue.setPadding(5);
                NBParameterPageTwoTableRowTwoPartTwo.addCell(tableTwoRowSevenCellFIveValue);

                PdfPCell tableTwoRowSevenCellSixValue = new PdfPCell(new Paragraph(tableTwoCellSix, small_bold));
                tableTwoRowSevenCellSixValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableTwoRowSevenCellSixValue.setPadding(5);
                NBParameterPageTwoTableRowTwoPartTwo.addCell(tableTwoRowSevenCellSixValue);

                PdfPCell tableTwoRowSevenCellSevenValue = new PdfPCell(new Paragraph(i + "", small_bold));
                tableTwoRowSevenCellSevenValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableTwoRowSevenCellSevenValue.setPadding(5);
                NBParameterPageTwoTableRowTwoPartTwo.addCell(tableTwoRowSevenCellSevenValue);

                PdfPCell tableTwoRowThreeCellEightValue = new PdfPCell(new Paragraph(tableTwoCellEight, small_bold));
                tableTwoRowThreeCellEightValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableTwoRowThreeCellEightValue.setPadding(5);
                NBParameterPageTwoTableRowTwoPartTwo.addCell(tableTwoRowThreeCellEightValue);

                PdfPCell tableTwoRowSevenCellNineValue = new PdfPCell(new Paragraph(i + "", small_bold));
                tableTwoRowSevenCellNineValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableTwoRowSevenCellNineValue.setPadding(5);
                NBParameterPageTwoTableRowTwoPartTwo.addCell(tableTwoRowSevenCellNineValue);

                PdfPCell tableTwoRowThreeCellTenValue = new PdfPCell(new Paragraph(tableTwoCellTen, small_bold));
                tableTwoRowThreeCellTenValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableTwoRowThreeCellTenValue.setPadding(5);
                NBParameterPageTwoTableRowTwoPartTwo.addCell(tableTwoRowThreeCellTenValue);

                //End Static Table Two Page Two


                //Static Table Three Page Two
                PdfPCell tableThreeRowThreeCellOneValue = new PdfPCell(new Paragraph(i + "", small_bold));
                tableThreeRowThreeCellOneValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableThreeRowThreeCellOneValue.setPadding(5);
                busQualityParameterPageTwoTableRowOne.addCell(tableThreeRowThreeCellOneValue);


                PdfPCell tableThreeParamRowThreeCellTwoValue = new PdfPCell(new Paragraph(tableThreeCellTwo, small_bold));
                tableThreeParamRowThreeCellTwoValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableThreeParamRowThreeCellTwoValue.setPadding(5);
                busQualityParameterPageTwoTableRowOne.addCell(tableThreeParamRowThreeCellTwoValue);

                PdfPCell tableThreeParamRowSevenCellThreeValue = new PdfPCell(new Paragraph(i + "", small_bold));
                tableThreeParamRowSevenCellThreeValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableThreeParamRowSevenCellThreeValue.setPadding(5);
                busQualityParameterPageTwoTableRowOne.addCell(tableThreeParamRowSevenCellThreeValue);

                PdfPCell tableThreeRowSevenCellFourValue = new PdfPCell(new Paragraph(tableThreeCellFour, small_bold));
                tableThreeRowSevenCellFourValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableThreeRowSevenCellFourValue.setPadding(5);
                busQualityParameterPageTwoTableRowOne.addCell(tableThreeRowSevenCellFourValue);

                PdfPCell tableThreeParamRowSevenCellFIveValue = new PdfPCell(new Paragraph(i + "", small_bold));
                tableThreeParamRowSevenCellFIveValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableThreeParamRowSevenCellFIveValue.setPadding(5);
                busQualityParameterPageTwoTableRowOne.addCell(tableThreeParamRowSevenCellFIveValue);

                PdfPCell tableThreeRowSevenCellSixValue = new PdfPCell(new Paragraph(tableThreeCellSix, small_bold));
                tableThreeRowSevenCellSixValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableThreeRowSevenCellSixValue.setPadding(5);
                busQualityParameterPageTwoTableRowOne.addCell(tableThreeRowSevenCellSixValue);

                PdfPCell tableThreeParamRowSevenCellSevenValue = new PdfPCell(new Paragraph(i + "", small_bold));
                tableThreeParamRowSevenCellSevenValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableThreeParamRowSevenCellSevenValue.setPadding(5);
                busQualityParameterPageTwoTableRowOne.addCell(tableThreeParamRowSevenCellSevenValue);

                PdfPCell tableThreeRowSevenCellEightValue = new PdfPCell(new Paragraph(tableThreeCellEight, small_bold1));
                tableThreeRowSevenCellEightValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableThreeRowSevenCellEightValue.setPadding(5);
                busQualityParameterPageTwoTableRowOne.addCell(tableThreeRowSevenCellEightValue);

                PdfPCell tableThreeParamRowSevenCellNineValue = new PdfPCell(new Paragraph(i + "", small_bold));
                tableThreeParamRowSevenCellNineValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableThreeParamRowSevenCellNineValue.setPadding(5);
                busQualityParameterPageTwoTableRowOne.addCell(tableThreeParamRowSevenCellNineValue);

                PdfPCell tableThreeRowSevenCellTenValue = new PdfPCell(new Paragraph(tableThreeCellten, small_bold));
                tableThreeRowSevenCellTenValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableThreeRowSevenCellTenValue.setPadding(5);
                busQualityParameterPageTwoTableRowOne.addCell(tableThreeRowSevenCellTenValue);

                PdfPCell tableThreeParamRowSevenCellElevenValue = new PdfPCell(new Paragraph(i + "", small_bold));
                tableThreeParamRowSevenCellElevenValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableThreeParamRowSevenCellElevenValue.setPadding(5);
                busQualityParameterPageTwoTableRowOne.addCell(tableThreeParamRowSevenCellElevenValue);

                PdfPCell tableThreeRowSevenCellTwelveValue = new PdfPCell(new Paragraph(tableThreeCellTwelve, small_bold));
                tableThreeRowSevenCellTwelveValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableThreeRowSevenCellTwelveValue.setPadding(5);
                busQualityParameterPageTwoTableRowOne.addCell(tableThreeRowSevenCellTwelveValue);

                //End Table Three Page Two

                //Static Table Four Page Two
                PdfPCell tableFourParamRowThreeCellOneValue = new PdfPCell(new Paragraph(i + "", small_bold));
                tableFourParamRowThreeCellOneValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableFourParamRowThreeCellOneValue.setPadding(5);
                PSParameterPageTwoTableTwoPartTwo.addCell(tableFourParamRowThreeCellOneValue);


                PdfPCell tableFourParamRowThreeCellTwoValue = new PdfPCell(new Paragraph(tableFourCellTwo, small_bold));
                tableFourParamRowThreeCellTwoValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableFourParamRowThreeCellTwoValue.setPadding(5);
                PSParameterPageTwoTableTwoPartTwo.addCell(tableFourParamRowThreeCellTwoValue);

                PdfPCell tableFourParamRowThreeCellThreeValue = new PdfPCell(new Paragraph(i + "", small_bold));
                tableFourParamRowThreeCellThreeValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableFourParamRowThreeCellThreeValue.setPadding(5);
                PSParameterPageTwoTableTwoPartTwo.addCell(tableFourParamRowThreeCellThreeValue);

                PdfPCell tableFourParamRowThreeCellFourValue = new PdfPCell(new Paragraph(tableFourCellFour, small_bold));
                tableFourParamRowThreeCellFourValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableFourParamRowThreeCellFourValue.setPadding(5);
                PSParameterPageTwoTableTwoPartTwo.addCell(tableFourParamRowThreeCellFourValue);

                PdfPCell tableFourParamRowThreeCellFIveValue = new PdfPCell(new Paragraph(i + "", small_bold));
                tableFourParamRowThreeCellFIveValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableFourParamRowThreeCellFIveValue.setPadding(5);
                PSParameterPageTwoTableTwoPartTwo.addCell(tableFourParamRowThreeCellFIveValue);

                PdfPCell tableFourParamRowThreeCellSixValue = new PdfPCell(new Paragraph(tableFourCellSix, small_bold));
                tableFourParamRowThreeCellSixValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableFourParamRowThreeCellSixValue.setPadding(5);
                PSParameterPageTwoTableTwoPartTwo.addCell(tableFourParamRowThreeCellSixValue);

                PdfPCell tableFourParamRowThreeCellSevenValue = new PdfPCell(new Paragraph(i + "", small_bold));
                tableFourParamRowThreeCellSevenValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableFourParamRowThreeCellSevenValue.setPadding(5);
                PSParameterPageTwoTableTwoPartTwo.addCell(tableFourParamRowThreeCellSevenValue);

                PdfPCell tableFourParamRowThreeCellEightValue = new PdfPCell(new Paragraph(tableFourCellEight, small_bold));
                tableFourParamRowThreeCellEightValue.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableFourParamRowThreeCellEightValue.setPadding(5);
                PSParameterPageTwoTableTwoPartTwo.addCell(tableFourParamRowThreeCellEightValue);


                //End Static Table Four Page Two
            }

            //End Row One part 1


            //Main Table
            PdfPTable digitalNBPageTwoMainTable = new PdfPTable(2);
            digitalNBPageTwoMainTable.setSpacingBefore(2f);
            digitalNBPageTwoMainTable.setWidthPercentage(100);

            PdfPCell digitalNBPageTwoHeaderCell;

            digitalNBPageTwoHeaderCell = new PdfPCell(digitalParameterPageTwoTableOne);
            digitalNBPageTwoHeaderCell.setPadding(5);
            digitalNBPageTwoHeaderCell.setBorder(Rectangle.NO_BORDER);
            digitalNBPageTwoHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            digitalNBPageTwoMainTable.addCell(digitalNBPageTwoHeaderCell);

            digitalNBPageTwoHeaderCell = new PdfPCell(NBParameterPageTwoTableRowTwoPartTwo);
            digitalNBPageTwoHeaderCell.setPadding(5);
            digitalNBPageTwoHeaderCell.setBorder(Rectangle.NO_BORDER);
            digitalNBPageTwoHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            digitalNBPageTwoMainTable.addCell(digitalNBPageTwoHeaderCell);

            document.add(digitalNBPageTwoMainTable);

            //NB parameter Part Two
            //End Digital Param Part One Page Two

            document.add(blankPDFTable);
            document.add(blankPDFTable);


            //Main Table
            PdfPTable businessPSPageTwoMainTable = new PdfPTable(2);
            businessPSPageTwoMainTable.setSpacingBefore(2f);
            businessPSPageTwoMainTable.setWidthPercentage(100);

            PdfPCell businessPSPagetwoHeaderCell;

            businessPSPagetwoHeaderCell = new PdfPCell(busQualityParameterPageTwoTableRowOne);
            businessPSPagetwoHeaderCell.setPadding(5);
            businessPSPagetwoHeaderCell.setBorder(Rectangle.NO_BORDER);
            businessPSPagetwoHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            businessPSPageTwoMainTable.addCell(businessPSPagetwoHeaderCell);

            businessPSPagetwoHeaderCell = new PdfPCell(PSParameterPageTwoTableTwoPartTwo);
            businessPSPagetwoHeaderCell.setPadding(5);
            businessPSPagetwoHeaderCell.setBorder(Rectangle.NO_BORDER);
            businessPSPagetwoHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            businessPSPageTwoMainTable.addCell(businessPSPagetwoHeaderCell);

            document.add(businessPSPageTwoMainTable);
            //End Business Quality Parameters Page Two

            document.add(blankPDFTable);
            document.add(blankPDFTable);

           /* PdfPTable disclaimerPageTwoPDFTable = new PdfPTable(1);
            disclaimerPageTwoPDFTable.setWidthPercentage(100);
            disclaimerTableCell = new PdfPCell(new Paragraph("Disclaimer :- Additional parameters will be added in subsequent score card.", small_bold));
            disclaimerTableCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            disclaimerTableCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            disclaimerTableCell.setPadding(5);
            disclaimerPageTwoPDFTable.addCell(disclaimerTableCell);
            document.add(disclaimerPageTwoPDFTable);*/

            document.add(blankPDFTable);

            document.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void tempMethod() {
        try {


        } catch (Exception E) {
            E.printStackTrace();
        }
        //NB parameter Part Two
        //End Digital Param Part One
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

        if (downloadScoreCardAsync != null) {
            downloadScoreCardAsync.cancel(true);
        }

        if (service != null) {
            service.cancel(true);
        }
    }

    private void service_hits() {


        service = new ServiceHits(context,
                METHOD_NAME_SCORE_CARD, strCIFBDMUserId,
                strCIFBDMUserId,
                strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();

    }
}
