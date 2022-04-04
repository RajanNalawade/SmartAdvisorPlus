package sbilife.com.pointofsale_bancaagency.cifenrollment;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.CaptureSignature;
import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;

public class ScoreCardQuationariesActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private CommonMethods mCommonMethods;
    private final int SIGNATURE_ACTIVITY = 1;
    private String str_urn_no = "", str_customer_sign = "";

    private ImageButton imgbtn_tcc_query_cust_sign;

    private CheckBox chk_tcc_que_a, chk_tcc_que_b, chk_tcc_que_c, chk_tcc_que_d, chk_tcc_que_e, chk_tcc_que_f,
            chk_tcc_que_g, chk_tcc_que_h, chk_tcc_que_i, chk_tcc_que_j, chk_tcc_que_k, chk_tcc_que_l, chk_tcc_que_m,
            chk_tcc_que_n, chk_tcc_que_o;

    private EditText edt_tcc_query_place/*, edt_tcc_query_name*/, edt_score_card_urn;
    private TextView txt_tcc_query_date;
    private Button btn_validate_urn;

    private ProgressDialog mProgressDialog;
    private AsynchValidateURN mAsynchValidateURN;

    private final String NAMESPACE = "http://tempuri.org/";
    private final String METHOD_NAME_SCORE_CARD_VALIDATE_URN = "validateURN";
    private final String METHOD_NAME_DOWNLOAD_SIGN = "getSignCIFenroll_smrt";

    private RelativeLayout rl_score_card_questions;
    private CommonMethods commonMethods;
    private Context context;
    private String strCIFBDMUserId = "", strCIFBDMMObileNo = "", strCIFBDMPassword ,strCIFBDMEmailId = "", userType;
    private final String URl = ServiceURL.SERVICE_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.fragment_tcc_quaries_upload);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.cifenrollment_pf_activity_title);

        //str_urn_no = getIntent().getStringExtra("URN");
        context = this;
        commonMethods = new CommonMethods();
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods
                .setUserDetails(context);
        if(userDetailsValuesModel != null){
            strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
            strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
            strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
        }



        initialisation();
    }

    private void initialisation() {

        mContext = this;
        mCommonMethods = new CommonMethods();
        DatabaseHelper db = new DatabaseHelper(mContext);

        mCommonMethods.setApplicationToolbarMenu1(this, "CIF on Boarding");

        edt_score_card_urn = findViewById(R.id.edt_score_card_urn);
        btn_validate_urn = findViewById(R.id.btn_validate_urn);
        btn_validate_urn.setOnClickListener(this);

        rl_score_card_questions = findViewById(R.id.rl_score_card_questions);

        Button btn_tcc_queries_submit = findViewById(R.id.btn_tcc_queries_submit);

        chk_tcc_que_a = findViewById(R.id.chk_tcc_que_a);
        chk_tcc_que_b = findViewById(R.id.chk_tcc_que_b);
        chk_tcc_que_c = findViewById(R.id.chk_tcc_que_c);
        chk_tcc_que_d = findViewById(R.id.chk_tcc_que_d);
        chk_tcc_que_e = findViewById(R.id.chk_tcc_que_e);
        chk_tcc_que_f = findViewById(R.id.chk_tcc_que_f);
        chk_tcc_que_g = findViewById(R.id.chk_tcc_que_g);
        chk_tcc_que_h = findViewById(R.id.chk_tcc_que_h);
        chk_tcc_que_i = findViewById(R.id.chk_tcc_que_i);
        chk_tcc_que_j = findViewById(R.id.chk_tcc_que_j);
        chk_tcc_que_k = findViewById(R.id.chk_tcc_que_k);
        chk_tcc_que_l = findViewById(R.id.chk_tcc_que_l);
        chk_tcc_que_m = findViewById(R.id.chk_tcc_que_m);
        chk_tcc_que_n = findViewById(R.id.chk_tcc_que_n);
        chk_tcc_que_o = findViewById(R.id.chk_tcc_que_o);

        /*edt_tcc_query_name = (EditText) findViewById(R.id.edt_tcc_query_name);*/
        edt_tcc_query_place = findViewById(R.id.edt_tcc_query_place);
        txt_tcc_query_date = findViewById(R.id.txt_tcc_query_date);
        imgbtn_tcc_query_cust_sign = findViewById(R.id.imgbtn_tcc_query_cust_sign);

        imgbtn_tcc_query_cust_sign.setOnClickListener(this);
        btn_tcc_queries_submit.setOnClickListener(this);

        Calendar cal = Calendar.getInstance();
        String mont = ((cal.get(Calendar.MONTH) + 1) < 10 ? "0" : "") + (cal.get(Calendar.MONTH) + 1);
        String day = (cal.get(Calendar.DAY_OF_MONTH) < 10 ? "0" : "") + cal.get(Calendar.DAY_OF_MONTH);

        txt_tcc_query_date.setText(day + "-" + mont + "-" + cal.get(Calendar.YEAR));

    }

    public class AsyncCreatePDF extends AsyncTask<String, String, String>{

        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Loading Please wait...";
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            running = create_tcc_questions_Pdf();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running){
                Intent mIntent = new Intent(ScoreCardQuationariesActivity.this, ScoreCardDeclarationActivity.class);
                mIntent.putExtra("URN", str_urn_no);
                mIntent.putExtra("PLACE", edt_tcc_query_place.getText().toString());
                startActivity(mIntent);
            }else{
                mCommonMethods.printLog("Error", "Question PDF");
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_validate_urn:

                if (!edt_score_card_urn.getText().toString().replaceAll("\\s+", "").trim().equals("")){

                    str_urn_no = edt_score_card_urn.getText().toString().replaceAll("\\s+", "").trim();

                    //validate urn
                    mAsynchValidateURN = new AsynchValidateURN();
                    mAsynchValidateURN.execute();

                }else{
                    mCommonMethods.showToast(mContext, "Please Enter URN Number");
                }

                break;

            case R.id.btn_tcc_queries_submit:
                //1. Create pdf

                if (!edt_tcc_query_place.getText().toString().replaceAll("\\s+", "").trim().equals("")) {
                    /*& !edt_tcc_query_name.getText().toString().replaceAll("\\s+", "").trim().equals("")*/

//                    ---------------Commented by bharamu--------------
//                    if (!str_customer_sign.equals("")) {
//
//                        /*create_tcc_questions_Pdf();*/
//
//                    /*Fragment_TCC_Declaration declarationFragment = new Fragment_TCC_Declaration();
//                    Bundle args = new Bundle();
//                    args.putString("URN", str_urn_no);
//                    args.putString("PLACE", edt_tcc_query_place.getText().toString());
//                    declarationFragment.setArguments(args);
//
//                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                    transaction.replace(R.id.frag_tcc_exam_details_container, declarationFragment);
//                    transaction.commit();
//                    */
//
//                        /*Intent mIntent = new Intent(ScoreCardQuationariesActivity.this, ScoreCardDeclarationActivity.class);
//                        mIntent.putExtra("URN", str_urn_no);
//                        mIntent.putExtra("PLACE", edt_tcc_query_place.getText().toString());
//                        startActivity(mIntent);*/
//
//                        new AsyncCreatePDF().execute();
//
//                    } else {
//                        mCommonMethods.showMessageDialog(mContext, "Blank singature!");
//                    }
//                    ----------------till here------------------
                    if (mCommonMethods.isNetworkConnected(mContext)){
                        new DownloadSignature().execute();
                    } else {
                        mCommonMethods.showToast(mContext, mCommonMethods.NO_INTERNET_MESSAGE);
                    }

                } else {
                    mCommonMethods.showMessageDialog(mContext, "Please enter place.");
                }
                break;

            case R.id.imgbtn_tcc_query_cust_sign:
                Intent intent = new Intent(mContext, CaptureSignature.class);
                startActivityForResult(intent, SIGNATURE_ACTIVITY);
                break;

            default:
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGNATURE_ACTIVITY) {
            if (resultCode == RESULT_OK) {

                Bundle bundle = data.getExtras();
                String status = bundle.getString("status");
                if (status != null && status.equalsIgnoreCase("done")) {
                    mCommonMethods.showToast(mContext, "Signature capture successful!");
                    imgbtn_tcc_query_cust_sign.setImageBitmap(CaptureSignature.scaled);
                    Bitmap signature = CaptureSignature.scaled;
                    if (signature != null) {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        signature.compress(Bitmap.CompressFormat.PNG, 100, out);
                        byte[] signByteArray = out.toByteArray();
                        str_customer_sign = Base64.encodeToString(signByteArray, Base64.DEFAULT);
                    } else {
                        mCommonMethods.showToast(mContext, "Null object..");
                    }
                }

            }
        }
    }

    class DownloadSignature extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String strRevivalListErrorCOde1 = "";
        private SoapObject soapObject;
        private SoapPrimitive soapPrimitive;
        private String inputpolicylist;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        }


        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_DOWNLOAD_SIGN);
                request.addProperty("strURN", str_urn_no);

                //commonMethods.appendSecurityParams(context,request,strCIFBDMEmailId,strCIFBDMMObileNo);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                androidHttpTranport.call(NAMESPACE + METHOD_NAME_DOWNLOAD_SIGN,
                        envelope);

                if(envelope.getResponse() instanceof SoapObject){
                    soapObject = (SoapObject) envelope.getResponse();
                    inputpolicylist = soapObject.toString();
                }else {
                    soapPrimitive = (SoapPrimitive) envelope.getResponse();
                    inputpolicylist = soapPrimitive.toString();
                }

//                SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();
//                String inputpolicylist = sa.toString();

                if (inputpolicylist != null && !inputpolicylist.equalsIgnoreCase("")) {
                    str_customer_sign = inputpolicylist;
                    CaptureSignature.scaled = base64ToBitmap(str_customer_sign);
                    strRevivalListErrorCOde1 = "success";
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
                    new AsyncCreatePDF().execute();
                } else {
                    commonMethods.showMessageDialog(context, "Signature is not uploaded, Please upload through Document reupload tab");
                }
            } else {
                commonMethods.showMessageDialog(context, "signature is not uploaded, Please upload through Document reupload tab");
            }
        }
    }

    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    private boolean create_tcc_questions_Pdf() {

        boolean isError = true;

        try {

            Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
            Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);
            Font small_bold1 = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);
            Font small_bold2 = new Font(Font.FontFamily.TIMES_ROMAN, 4, Font.BOLD);
            Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL);

            File quesFile = new StorageUtils().createFileToAppSpecificDirCIF(context,
                    "questions_" + str_urn_no + ".pdf");

            Rectangle rect = new Rectangle(594f, 792f);

            Document document = new Document(rect, 50, 50, 50, 50);
            // Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter pdf_writer = null;
            pdf_writer = PdfWriter.getInstance(document, new FileOutputStream(quesFile.getAbsolutePath()));

            document.open();
            /*// For SBI- Life Logo starts
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.sbi_life_logo);
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
            Image img_sbi_logo = Image.getInstance(stream.toByteArray());
            img_sbi_logo.setAlignment(Image.LEFT);
            img_sbi_logo.getSpacingAfter();
            img_sbi_logo.scaleToFit(80, 50);

            Paragraph para_img_logo = new Paragraph("");
            para_img_logo.add(img_sbi_logo);

            document.add(para_img_logo);
            // For SBI- Life Logo ends*/

            Paragraph para_img_logo_after_space_1 = new Paragraph(" ");

            // To draw line after the sbi logo image
            /*document.add(new LineSeparator());
            document.add(para_img_logo_after_space_1);*/

            //to tittle 1
            Paragraph para_tittle_1 = new Paragraph("Annexture - 1", headerBold);
            para_tittle_1.setAlignment(Element.ALIGN_RIGHT);
            document.add(para_tittle_1);
            document.add(para_img_logo_after_space_1);

            Paragraph para_tittle_1_a = new Paragraph("[See regulafion 7(2)(g)]", headerBold);
            para_tittle_1_a.setAlignment(Element.ALIGN_CENTER);
            document.add(para_tittle_1_a);
            document.add(para_img_logo_after_space_1);

            //to tittle 2
            Paragraph para_tittle_2 = new Paragraph("INSURANCE REGULATORY AND DEVELOPMENT AUTHORITY OF INDIA (REGISTRATION OF CORPORATE AGENT) REGULATIONS, 2015", small_bold);
            para_tittle_2.setAlignment(Element.ALIGN_CENTER);
            document.add(para_tittle_2);
            document.add(para_img_logo_after_space_1);

            //to tittle 3
            Paragraph para_tittle_3 = new Paragraph("Declaration and Undertaking of Specified Persons/CIF (A separate form needs to be submitted by each individual)", small_normal);
            para_tittle_3.setAlignment(Element.ALIGN_LEFT);
            document.add(para_tittle_3);
            document.add(para_img_logo_after_space_1);

            //to Question details
            PdfPTable BI_PdfQuestionDetails = new PdfPTable(3);
            BI_PdfQuestionDetails.setWidthPercentage(100);
            BI_PdfQuestionDetails.setWidths(new int[]{1, 2, 1});

            PdfPCell BI_PdftableSrNo_cell = new PdfPCell(new Paragraph("Sr. No.", small_normal));
            BI_PdftableSrNo_cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableSrNo_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell.setPadding(5);

            PdfPCell BI_PdftableQuestion_cell = new PdfPCell(new Paragraph("Fit & Proper Certificate", small_normal));
            BI_PdftableQuestion_cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableQuestion_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableQuestion_cell.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableQuestion_cell.setPadding(5);

            PdfPCell BI_PdftableAns_cell = new PdfPCell(new Paragraph("Yes/No", small_normal));
            BI_PdftableAns_cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableAns_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell.setPadding(5);

            BI_PdfQuestionDetails.addCell(BI_PdftableSrNo_cell);
            BI_PdfQuestionDetails.addCell(BI_PdftableQuestion_cell);
            BI_PdfQuestionDetails.addCell(BI_PdftableAns_cell);
            document.add(BI_PdfQuestionDetails);

            //to Question details a
            PdfPTable BI_PdfQuestionDetails_a = new PdfPTable(3);
            BI_PdfQuestionDetails_a.setWidthPercentage(100);
            BI_PdfQuestionDetails_a.setWidths(new int[]{1, 2, 1});

            PdfPCell BI_PdftableSrNo_cell_a = new PdfPCell(new Paragraph("a)", small_normal));
            //BI_PdftableSrNo_cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableSrNo_cell_a.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_a.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_a.setPadding(5);

            PdfPCell BI_PdftableQuestion_cell_a = new PdfPCell(new Paragraph("Have you ever registered or obtained license from any of the regulatory authorities under any law such as SEBI, RBI, IRDA, PFRDA, FMC etc.?", small_normal));
            //BI_PdftableQuestion_cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            //BI_PdftableQuestion_cell_a.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableQuestion_cell_a.setPadding(5);

            PdfPCell BI_PdftableAns_cell_a;
            if (chk_tcc_que_a.isChecked())
                BI_PdftableAns_cell_a = new PdfPCell(new Paragraph("Yes", small_normal));
            else
                BI_PdftableAns_cell_a = new PdfPCell(new Paragraph("No", small_normal));
            //BI_PdftableAns_cell_a.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableAns_cell_a.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_a.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_a.setPadding(5);

            BI_PdfQuestionDetails_a.addCell(BI_PdftableSrNo_cell_a);
            BI_PdfQuestionDetails_a.addCell(BI_PdftableQuestion_cell_a);
            BI_PdfQuestionDetails_a.addCell(BI_PdftableAns_cell_a);
            document.add(BI_PdfQuestionDetails_a);

            //to Question details b
            PdfPTable BI_PdfQuestionDetails_b = new PdfPTable(3);
            BI_PdfQuestionDetails_b.setWidthPercentage(100);
            BI_PdfQuestionDetails_b.setWidths(new int[]{1, 2, 1});

            PdfPCell BI_PdftableSrNo_cell_b = new PdfPCell(new Paragraph("b)", small_normal));
            //BI_PdftableSrNo_cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableSrNo_cell_b.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_b.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_b.setPadding(5);

            PdfPCell BI_PdftableQuestion_cell_b = new PdfPCell(new Paragraph("Have you carried on business under any name other than the name stated in this application?", small_normal));
            //BI_PdftableQuestion_cell_b.setBackgroundColor(BaseColor.LIGHT_GRAY);
            //BI_PdftableQuestion_cell_b.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableQuestion_cell_b.setPadding(5);

            PdfPCell BI_PdftableAns_cell_b;
            if (chk_tcc_que_b.isChecked())
                BI_PdftableAns_cell_b = new PdfPCell(new Paragraph("Yes", small_normal));
            else
                BI_PdftableAns_cell_b = new PdfPCell(new Paragraph("No", small_normal));

            //BI_PdftableAns_cell_b.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableAns_cell_b.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_b.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_b.setPadding(5);

            BI_PdfQuestionDetails_b.addCell(BI_PdftableSrNo_cell_b);
            BI_PdfQuestionDetails_b.addCell(BI_PdftableQuestion_cell_b);
            BI_PdfQuestionDetails_b.addCell(BI_PdftableAns_cell_b);
            document.add(BI_PdfQuestionDetails_b);

            //to Question details c
            PdfPTable BI_PdfQuestionDetails_c = new PdfPTable(3);
            BI_PdfQuestionDetails_c.setWidthPercentage(100);
            BI_PdfQuestionDetails_c.setWidths(new int[]{1, 2, 1});

            PdfPCell BI_PdftableSrNo_cell_c = new PdfPCell(new Paragraph("c)", small_normal));
            //BI_PdftableSrNo_cell_c.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableSrNo_cell_c.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_c.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_c.setPadding(5);

            PdfPCell BI_PdftableQuestion_cell_c = new PdfPCell(new Paragraph("Have you ever been refused or restricted by any regulatory authority to carry on any business, trade or profession for which a specific license registration or other authorization is required by law?", small_normal));
            //BI_PdftableQuestion_cell_c.setBackgroundColor(BaseColor.LIGHT_GRAY);
            //BI_PdftableQuestion_cell_c.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableQuestion_cell_c.setPadding(5);

            PdfPCell BI_PdftableAns_cell_c;
            if (chk_tcc_que_c.isChecked())
                BI_PdftableAns_cell_c = new PdfPCell(new Paragraph("Yes", small_normal));
            else
                BI_PdftableAns_cell_c = new PdfPCell(new Paragraph("No", small_normal));

            //BI_PdftableAns_cell_c.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableAns_cell_c.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_c.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_c.setPadding(5);

            BI_PdfQuestionDetails_c.addCell(BI_PdftableSrNo_cell_c);
            BI_PdfQuestionDetails_c.addCell(BI_PdftableQuestion_cell_c);
            BI_PdfQuestionDetails_c.addCell(BI_PdftableAns_cell_c);
            document.add(BI_PdfQuestionDetails_c);

            //to Question details
            PdfPTable BI_PdfQuestionDetails_d = new PdfPTable(3);
            BI_PdfQuestionDetails_d.setWidthPercentage(100);
            BI_PdfQuestionDetails_d.setWidths(new int[]{1, 2, 1});

            PdfPCell BI_PdftableSrNo_cell_d = new PdfPCell(new Paragraph("d)", small_normal));
            //BI_PdftableSrNo_cell_d.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableSrNo_cell_d.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_d.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_d.setPadding(5);

            PdfPCell BI_PdftableQuestion_cell_d = new PdfPCell(new Paragraph("Have you been ever censured or disciplined or suspended or refused permission or license or registration by any regulatory authority to carry on any business activity?", small_normal));
            //BI_PdftableQuestion_cell_d.setBackgroundColor(BaseColor.LIGHT_GRAY);
            //BI_PdftableQuestion_cell_d.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableQuestion_cell_d.setPadding(5);

            PdfPCell BI_PdftableAns_cell_d;
            if (chk_tcc_que_d.isChecked())
                BI_PdftableAns_cell_d = new PdfPCell(new Paragraph("Yes", small_normal));
            else
                BI_PdftableAns_cell_d = new PdfPCell(new Paragraph("No", small_normal));
            //BI_PdftableAns_cell_d.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableAns_cell_d.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_d.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_d.setPadding(5);

            BI_PdfQuestionDetails_d.addCell(BI_PdftableSrNo_cell_d);
            BI_PdfQuestionDetails_d.addCell(BI_PdftableQuestion_cell_d);
            BI_PdfQuestionDetails_d.addCell(BI_PdftableAns_cell_d);
            document.add(BI_PdfQuestionDetails_d);

            //to Question details e
            PdfPTable BI_PdfQuestionDetails_e = new PdfPTable(3);
            BI_PdfQuestionDetails_e.setWidthPercentage(100);
            BI_PdfQuestionDetails_e.setWidths(new int[]{1, 2, 1});

            PdfPCell BI_PdftableSrNo_cell_e = new PdfPCell(new Paragraph("e)", small_normal));
            //BI_PdftableSrNo_cell_e.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableSrNo_cell_e.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_e.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_e.setPadding(5);

            PdfPCell BI_PdftableQuestion_cell_e = new PdfPCell(new Paragraph("Have you been subject to any investigations or disciplinary proceeding or have been issued warning or reprimand by any regulatory authority?", small_normal));
            //BI_PdftableQuestion_cell_e.setBackgroundColor(BaseColor.LIGHT_GRAY);
            //BI_PdftableQuestion_cell_e.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableQuestion_cell_e.setPadding(5);

            PdfPCell BI_PdftableAns_cell_e;
            if (chk_tcc_que_e.isChecked())
                BI_PdftableAns_cell_e = new PdfPCell(new Paragraph("Yes", small_normal));
            else
                BI_PdftableAns_cell_e = new PdfPCell(new Paragraph("No", small_normal));

            //BI_PdftableAns_cell_e.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableAns_cell_e.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_e.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_e.setPadding(5);

            BI_PdfQuestionDetails_e.addCell(BI_PdftableSrNo_cell_e);
            BI_PdfQuestionDetails_e.addCell(BI_PdftableQuestion_cell_e);
            BI_PdfQuestionDetails_e.addCell(BI_PdftableAns_cell_e);
            document.add(BI_PdfQuestionDetails_e);

            //to Question details f
            PdfPTable BI_PdfQuestionDetails_f = new PdfPTable(3);
            BI_PdfQuestionDetails_f.setWidthPercentage(100);
            BI_PdfQuestionDetails_f.setWidths(new int[]{1, 2, 1});

            PdfPCell BI_PdftableSrNo_cell_f = new PdfPCell(new Paragraph("f)", small_normal));
            //BI_PdftableSrNo_cell_f.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableSrNo_cell_f.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_f.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_f.setPadding(5);

            PdfPCell BI_PdftableQuestion_cell_f = new PdfPCell(new Paragraph("Have you been convicted of any offence or subject to any pending proceedings under any law?", small_normal));
            //BI_PdftableQuestion_cell_f.setBackgroundColor(BaseColor.LIGHT_GRAY);
            //BI_PdftableQuestion_cell_f.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableQuestion_cell_f.setPadding(5);

            PdfPCell BI_PdftableAns_cell_f;
            if (chk_tcc_que_f.isChecked())
                BI_PdftableAns_cell_f = new PdfPCell(new Paragraph("Yes", small_normal));
            else
                BI_PdftableAns_cell_f = new PdfPCell(new Paragraph("No", small_normal));

            //BI_PdftableAns_cell_f.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableAns_cell_f.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_f.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_f.setPadding(5);

            BI_PdfQuestionDetails_f.addCell(BI_PdftableSrNo_cell_f);
            BI_PdfQuestionDetails_f.addCell(BI_PdftableQuestion_cell_f);
            BI_PdfQuestionDetails_f.addCell(BI_PdftableAns_cell_f);
            document.add(BI_PdfQuestionDetails_f);

            //to Question details g
            PdfPTable BI_PdfQuestionDetails_g = new PdfPTable(3);
            BI_PdfQuestionDetails_g.setWidthPercentage(100);
            BI_PdfQuestionDetails_g.setWidths(new int[]{1, 2, 1});

            PdfPCell BI_PdftableSrNo_cell_g = new PdfPCell(new Paragraph("g)", small_normal));
            //BI_PdftableSrNo_cell_g.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableSrNo_cell_g.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_g.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_g.setPadding(5);

            PdfPCell BI_PdftableQuestion_cell_g = new PdfPCell(new Paragraph("Have you been banned from entry from any profession / occupation at any time?", small_normal));
            //BI_PdftableQuestion_cell_g.setBackgroundColor(BaseColor.LIGHT_GRAY);
            //BI_PdftableQuestion_cell_g.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableQuestion_cell_g.setPadding(5);

            PdfPCell BI_PdftableAns_cell_g;
            if (chk_tcc_que_g.isChecked())
                BI_PdftableAns_cell_g = new PdfPCell(new Paragraph("Yes", small_normal));
            else
                BI_PdftableAns_cell_g = new PdfPCell(new Paragraph("No", small_normal));
            //BI_PdftableAns_cell_g.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableAns_cell_g.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_g.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_g.setPadding(5);

            BI_PdfQuestionDetails_g.addCell(BI_PdftableSrNo_cell_g);
            BI_PdfQuestionDetails_g.addCell(BI_PdftableQuestion_cell_g);
            BI_PdfQuestionDetails_g.addCell(BI_PdftableAns_cell_g);
            document.add(BI_PdfQuestionDetails_g);

            //to Question details h
            PdfPTable BI_PdfQuestionDetails_h = new PdfPTable(3);
            BI_PdfQuestionDetails_h.setWidthPercentage(100);
            BI_PdfQuestionDetails_h.setWidths(new int[]{1, 2, 1});

            PdfPCell BI_PdftableSrNo_cell_h = new PdfPCell(new Paragraph("h)", small_normal));
            //BI_PdftableSrNo_cell_h.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableSrNo_cell_h.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_h.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_h.setPadding(5);

            PdfPCell BI_PdftableQuestion_cell_h = new PdfPCell(new Paragraph("Details of prosecution, if any, pending or commenced or resulting in conviction in the past for violation of economic laws and regulations?", small_normal));
            //BI_PdftableQuestion_cell_h.setBackgroundColor(BaseColor.LIGHT_GRAY);
            //BI_PdftableQuestion_cell_h.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableQuestion_cell_h.setPadding(5);

            PdfPCell BI_PdftableAns_cell_h;
            if (chk_tcc_que_h.isChecked())
                BI_PdftableAns_cell_h = new PdfPCell(new Paragraph("Yes", small_normal));
            else
                BI_PdftableAns_cell_h = new PdfPCell(new Paragraph("No", small_normal));
            //BI_PdftableAns_cell_h.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableAns_cell_h.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_h.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_h.setPadding(5);

            BI_PdfQuestionDetails_h.addCell(BI_PdftableSrNo_cell_h);
            BI_PdfQuestionDetails_h.addCell(BI_PdftableQuestion_cell_h);
            BI_PdfQuestionDetails_h.addCell(BI_PdftableAns_cell_h);
            document.add(BI_PdfQuestionDetails_h);

            //to Question details i
            PdfPTable BI_PdfQuestionDetails_i = new PdfPTable(3);
            BI_PdfQuestionDetails_i.setWidthPercentage(100);
            BI_PdfQuestionDetails_i.setWidths(new int[]{1, 2, 1});

            PdfPCell BI_PdftableSrNo_cell_i = new PdfPCell(new Paragraph("i)", small_normal));
            //BI_PdftableSrNo_cell_i.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableSrNo_cell_i.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_i.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_i.setPadding(5);

            PdfPCell BI_PdftableQuestion_cell_i = new PdfPCell(new Paragraph("Details of  criminal prosecution, if  any, pending or commenced or resulting in conviction in the past against the applicant?", small_normal));
            //BI_PdftableQuestion_cell_i.setBackgroundColor(BaseColor.LIGHT_GRAY);
            //BI_PdftableQuestion_cell_i.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableQuestion_cell_i.setPadding(5);

            PdfPCell BI_PdftableAns_cell_i;
            if (chk_tcc_que_i.isChecked())
                BI_PdftableAns_cell_i = new PdfPCell(new Paragraph("Yes", small_normal));
            else
                BI_PdftableAns_cell_i = new PdfPCell(new Paragraph("No", small_normal));
            //BI_PdftableAns_cell_i.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableAns_cell_i.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_i.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_i.setPadding(5);

            BI_PdfQuestionDetails_i.addCell(BI_PdftableSrNo_cell_i);
            BI_PdfQuestionDetails_i.addCell(BI_PdftableQuestion_cell_i);
            BI_PdfQuestionDetails_i.addCell(BI_PdftableAns_cell_i);
            document.add(BI_PdfQuestionDetails_i);

            //to Question details j
            PdfPTable BI_PdfQuestionDetails_j = new PdfPTable(3);
            BI_PdfQuestionDetails_j.setWidthPercentage(100);
            BI_PdfQuestionDetails_j.setWidths(new int[]{1, 2, 1});

            PdfPCell BI_PdftableSrNo_cell_j = new PdfPCell(new Paragraph("j)", small_normal));
            //BI_PdftableSrNo_cell_j.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableSrNo_cell_j.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_j.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_j.setPadding(5);

            PdfPCell BI_PdftableQuestion_cell_j = new PdfPCell(new Paragraph("Do you attract any of the disqualifications envisaged under Section 164 of the Companies’ Act 2013?", small_normal));
            //BI_PdftableQuestion_cell_j.setBackgroundColor(BaseColor.LIGHT_GRAY);
            //BI_PdftableQuestion_cell_j.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableQuestion_cell_j.setPadding(5);

            PdfPCell BI_PdftableAns_cell_j;
            if (chk_tcc_que_j.isChecked())
                BI_PdftableAns_cell_j = new PdfPCell(new Paragraph("Yes", small_normal));
            else
                BI_PdftableAns_cell_j = new PdfPCell(new Paragraph("No", small_normal));
            //BI_PdftableAns_cell_j.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableAns_cell_j.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_j.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_j.setPadding(5);

            BI_PdfQuestionDetails_j.addCell(BI_PdftableSrNo_cell_j);
            BI_PdfQuestionDetails_j.addCell(BI_PdftableQuestion_cell_j);
            BI_PdfQuestionDetails_j.addCell(BI_PdftableAns_cell_j);
            document.add(BI_PdfQuestionDetails_j);

            //to Question details k
            PdfPTable BI_PdfQuestionDetails_k = new PdfPTable(3);
            BI_PdfQuestionDetails_k.setWidthPercentage(100);
            BI_PdfQuestionDetails_k.setWidths(new int[]{1, 2, 1});

            PdfPCell BI_PdftableSrNo_cell_k = new PdfPCell(new Paragraph("k)", small_normal));
            //BI_PdftableSrNo_cell_k.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableSrNo_cell_k.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_k.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_k.setPadding(5);

            PdfPCell BI_PdftableQuestion_cell_k = new PdfPCell(new Paragraph("Have you been subject to any investigation at the instance of Government department or agency?", small_normal));
            //BI_PdftableQuestion_cell_k.setBackgroundColor(BaseColor.LIGHT_GRAY);
            //BI_PdftableQuestion_cell_k.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableQuestion_cell_k.setPadding(5);

            PdfPCell BI_PdftableAns_cell_k;
            if (chk_tcc_que_k.isChecked())
                BI_PdftableAns_cell_k = new PdfPCell(new Paragraph("Yes", small_normal));
            else
                BI_PdftableAns_cell_k = new PdfPCell(new Paragraph("No", small_normal));
            //BI_PdftableAns_cell_k.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableAns_cell_k.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_k.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_k.setPadding(5);

            BI_PdfQuestionDetails_k.addCell(BI_PdftableSrNo_cell_k);
            BI_PdfQuestionDetails_k.addCell(BI_PdftableQuestion_cell_k);
            BI_PdfQuestionDetails_k.addCell(BI_PdftableAns_cell_k);
            document.add(BI_PdfQuestionDetails_k);

            //to Question details l
            PdfPTable BI_PdfQuestionDetails_l = new PdfPTable(3);
            BI_PdfQuestionDetails_l.setWidthPercentage(100);
            BI_PdfQuestionDetails_l.setWidths(new int[]{1, 2, 1});

            PdfPCell BI_PdftableSrNo_cell_l = new PdfPCell(new Paragraph("l)", small_normal));
            //BI_PdftableSrNo_cell_l.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableSrNo_cell_l.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_l.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_l.setPadding(5);

            PdfPCell BI_PdftableQuestion_cell_l = new PdfPCell(new Paragraph("Have you at any time been found guilty of violation of rules / regulations / legislative requirements by customs / excise / income tax / foreign exchange / other revenue authorities, if so give particulars?", small_normal));
            //BI_PdftableQuestion_cell_l.setBackgroundColor(BaseColor.LIGHT_GRAY);
            //BI_PdftableQuestion_cell_l.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableQuestion_cell_l.setPadding(5);

            PdfPCell BI_PdftableAns_cell_l;
            if (chk_tcc_que_l.isChecked())
                BI_PdftableAns_cell_l = new PdfPCell(new Paragraph("Yes", small_normal));
            else
                BI_PdftableAns_cell_l = new PdfPCell(new Paragraph("No", small_normal));
            //BI_PdftableAns_cell_l.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableAns_cell_l.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_l.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_l.setPadding(5);

            BI_PdfQuestionDetails_l.addCell(BI_PdftableSrNo_cell_l);
            BI_PdfQuestionDetails_l.addCell(BI_PdftableQuestion_cell_l);
            BI_PdfQuestionDetails_l.addCell(BI_PdftableAns_cell_l);
            document.add(BI_PdfQuestionDetails_l);

            //to Question details m
            PdfPTable BI_PdfQuestionDetails_m = new PdfPTable(3);
            BI_PdfQuestionDetails_m.setWidthPercentage(100);
            BI_PdfQuestionDetails_m.setWidths(new int[]{1, 2, 1});

            PdfPCell BI_PdftableSrNo_cell_m = new PdfPCell(new Paragraph("m)", small_normal));
            //BI_PdftableSrNo_cell_m.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableSrNo_cell_m.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_m.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_m.setPadding(5);

            PdfPCell BI_PdftableQuestion_cell_m = new PdfPCell(new Paragraph("Have you at any time come to the adverse notice of a regulator such as SEBI, IRDA, MCA, PFRDA. (Though it shall not be necessary for a candidate to mention in the column about orders and findings made by regulators which have been later on reversed / set aside in toto,  it  would  be  necessary  to  make  a  mention  of  the same, in case the reversal / setting aside is on technical reasons like limitation or lack of jurisdiction, etc, and not on merit. If the order of the regulator is temporarily stayed and the appellate / court proceedings are pending, the same also should be mentioned)?", small_normal));
            //BI_PdftableQuestion_cell_m.setBackgroundColor(BaseColor.LIGHT_GRAY);
            //BI_PdftableQuestion_cell_m.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableQuestion_cell_m.setPadding(5);

            PdfPCell BI_PdftableAns_cell_m;
            if (chk_tcc_que_m.isChecked())
                BI_PdftableAns_cell_m = new PdfPCell(new Paragraph("Yes", small_normal));
            else
                BI_PdftableAns_cell_m = new PdfPCell(new Paragraph("No", small_normal));
            //BI_PdftableAns_cell_m.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableAns_cell_m.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_m.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_m.setPadding(5);

            BI_PdfQuestionDetails_m.addCell(BI_PdftableSrNo_cell_m);
            BI_PdfQuestionDetails_m.addCell(BI_PdftableQuestion_cell_m);
            BI_PdfQuestionDetails_m.addCell(BI_PdftableAns_cell_m);
            document.add(BI_PdfQuestionDetails_m);

            //to Question details n
            PdfPTable BI_PdfQuestionDetails_n = new PdfPTable(3);
            BI_PdfQuestionDetails_n.setWidthPercentage(100);
            BI_PdfQuestionDetails_n.setWidths(new int[]{1, 2, 1});

            PdfPCell BI_PdftableSrNo_cell_n = new PdfPCell(new Paragraph("n)", small_normal));
            //BI_PdftableSrNo_cell_n.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableSrNo_cell_n.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_n.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_n.setPadding(5);

            PdfPCell BI_PdftableQuestion_cell_n = new PdfPCell(new Paragraph("Has any of your group company/associate company/related party been carrying any license issued by the IRDA?", small_normal));
            //BI_PdftableQuestion_cell_n.setBackgroundColor(BaseColor.LIGHT_GRAY);
            //BI_PdftableQuestion_cell_n.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableQuestion_cell_n.setPadding(5);

            PdfPCell BI_PdftableAns_cell_n;
            if (chk_tcc_que_n.isChecked())
                BI_PdftableAns_cell_n = new PdfPCell(new Paragraph("Yes", small_normal));
            else
                BI_PdftableAns_cell_n = new PdfPCell(new Paragraph("No", small_normal));
            //BI_PdftableAns_cell_n.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableAns_cell_n.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_n.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_n.setPadding(5);

            BI_PdfQuestionDetails_n.addCell(BI_PdftableSrNo_cell_n);
            BI_PdfQuestionDetails_n.addCell(BI_PdftableQuestion_cell_n);
            BI_PdfQuestionDetails_n.addCell(BI_PdftableAns_cell_n);
            document.add(BI_PdfQuestionDetails_n);

            //sign at bottom
            byte[] fbyt_applicant = Base64.decode(str_customer_sign, 0);
            Bitmap applicantBitmap = BitmapFactory.decodeByteArray(fbyt_applicant, 0, fbyt_applicant.length);

            ByteArrayOutputStream customer_signature_stream = new ByteArrayOutputStream();

            (applicantBitmap).compress(Bitmap.CompressFormat.PNG, 50, customer_signature_stream);
            Image applicant_signature = Image.getInstance(customer_signature_stream.toByteArray());

            applicant_signature.scaleToFit(90, 90);

            PdfPTable BI_PdftableApplicant_sign = new PdfPTable(3);
            BI_PdftableApplicant_sign.setWidths(new float[]{5f, 5f, 5f});
            BI_PdftableApplicant_sign.setWidthPercentage(100);

            PdfPCell Nocell = new PdfPCell(new Paragraph("", small_bold));
            Nocell.setBorder(Rectangle.NO_BORDER);

            BI_PdftableApplicant_sign.addCell(Nocell);

            BI_PdftableApplicant_sign.addCell(Nocell);

            PdfPCell sign_cell = new PdfPCell(applicant_signature);
            sign_cell.setHorizontalAlignment(Element.ALIGN_TOP);
            sign_cell.setPadding(5);
            sign_cell.setBorder(Rectangle.NO_BORDER);
            BI_PdftableApplicant_sign.addCell(sign_cell);

            /*BI_PdftableApplicant_sign.addCell(Nocell);

            BI_PdftableApplicant_sign.addCell(Nocell);

            sign_cell = new PdfPCell(new Paragraph(
                    "Applicant Signature", small_bold));
            sign_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            sign_cell.setBorder(Rectangle.TOP);
            sign_cell.setPadding(5);
            BI_PdftableApplicant_sign.addCell(sign_cell);*/

            document.add(BI_PdftableApplicant_sign);

            //to Question details o
            PdfPTable BI_PdfQuestionDetails_o = new PdfPTable(3);
            BI_PdfQuestionDetails_o.setWidthPercentage(100);
            BI_PdfQuestionDetails_o.setWidths(new int[]{1, 2, 1});

            PdfPCell BI_PdftableSrNo_cell_o = new PdfPCell(new Paragraph("o)", small_normal));
            //BI_PdftableSrNo_cell_o.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableSrNo_cell_o.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_o.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableSrNo_cell_o.setPadding(5);

            PdfPCell BI_PdftableQuestion_cell_o = new PdfPCell(new Paragraph("Any other explanation / information in regard to items I and II  and other information considered relevant for judging fit and proper criteria of the applicant?", small_normal));
            //BI_PdftableQuestion_cell_o.setBackgroundColor(BaseColor.LIGHT_GRAY);
            //BI_PdftableQuestion_cell_o.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableQuestion_cell_o.setPadding(5);

            PdfPCell BI_PdftableAns_cell_o;
            if (chk_tcc_que_o.isChecked())
                BI_PdftableAns_cell_o = new PdfPCell(new Paragraph("Yes", small_normal));
            else
                BI_PdftableAns_cell_o = new PdfPCell(new Paragraph("No", small_normal));
            //BI_PdftableAns_cell_o.setBackgroundColor(BaseColor.LIGHT_GRAY);
            BI_PdftableAns_cell_o.setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_o.setVerticalAlignment(Element.ALIGN_CENTER);
            BI_PdftableAns_cell_o.setPadding(5);

            BI_PdfQuestionDetails_o.addCell(BI_PdftableSrNo_cell_o);
            BI_PdfQuestionDetails_o.addCell(BI_PdftableQuestion_cell_o);
            BI_PdfQuestionDetails_o.addCell(BI_PdftableAns_cell_o);
            document.add(BI_PdfQuestionDetails_o);

            // new page
            //document.newPage();

            //to declaration msg
            Paragraph para_declaration_msg = new Paragraph("I confirm that the above information is, to the best of my knowledge and belief, true and complete. I undertake to keep the Authority fully informed, as soon as possible, of all events, which take place subsequent to my appointment, which are relevant to the information provided above.", small_normal);
            para_declaration_msg.setAlignment(Element.ALIGN_LEFT);
            document.add(para_declaration_msg);
            document.add(para_img_logo_after_space_1);


            //place , date and signature
            PdfPTable BI_PdftableApplicant = new PdfPTable(3);
            BI_PdftableApplicant.setWidths(new float[]{5f, 5f, 5f});
            BI_PdftableApplicant.setWidthPercentage(100);

            //row 1
            sign_cell = new PdfPCell(new Paragraph(
                    "Name : " + ""/*edt_tcc_query_name.getText().toString()*/, small_bold));
            sign_cell.setHorizontalAlignment(Element.ALIGN_BOTTOM);
            sign_cell.setPadding(5);
            sign_cell.setBorder(Rectangle.NO_BORDER);
            BI_PdftableApplicant.addCell(sign_cell);

            BI_PdftableApplicant.addCell(Nocell);

            BI_PdftableApplicant.addCell(Nocell);

            //row 2
            sign_cell = new PdfPCell(new Paragraph(
                    "Designation : " + "", small_bold));
            sign_cell.setHorizontalAlignment(Element.ALIGN_BOTTOM);
            sign_cell.setPadding(5);
            sign_cell.setBorder(Rectangle.NO_BORDER);
            BI_PdftableApplicant.addCell(sign_cell);

            BI_PdftableApplicant.addCell(Nocell);

            BI_PdftableApplicant.addCell(Nocell);

            //row 3
            sign_cell = new PdfPCell(new Paragraph(
                    "Place : " + edt_tcc_query_place.getText().toString(), small_bold));
            sign_cell.setHorizontalAlignment(Element.ALIGN_BOTTOM);
            sign_cell.setPadding(5);
            sign_cell.setBorder(Rectangle.NO_BORDER);
            BI_PdftableApplicant.addCell(sign_cell);

            BI_PdftableApplicant.addCell(Nocell);

            sign_cell = new PdfPCell(applicant_signature);
            sign_cell.setHorizontalAlignment(Element.ALIGN_TOP);
            sign_cell.setPadding(5);
            sign_cell.setBorder(Rectangle.NO_BORDER);
            BI_PdftableApplicant.addCell(sign_cell);

            //row 4
            sign_cell = new PdfPCell(new Paragraph(
                    "Date : " + txt_tcc_query_date.getText().toString(), small_bold));
            sign_cell.setHorizontalAlignment(Element.ALIGN_TOP);
            sign_cell.setPadding(5);
            sign_cell.setBorder(Rectangle.NO_BORDER);
            BI_PdftableApplicant.addCell(sign_cell);

            BI_PdftableApplicant.addCell(Nocell);

            BI_PdftableApplicant.addCell(Nocell);

            /*sign_cell = new PdfPCell(new Paragraph(
                    "Applicant Signature", small_bold));
            sign_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            sign_cell.setBorder(Rectangle.TOP);
            sign_cell.setPadding(5);
            BI_PdftableApplicant.addCell(sign_cell);*/

            document.add(BI_PdftableApplicant);

            /*//place, date and sign
            PdfPTable BI_Pdftable_tcc_sign = new PdfPTable(6);
            BI_Pdftable_tcc_sign.setWidthPercentage(100);

            PdfPCell BI_Pdftable_tcc_place = new PdfPCell(new Paragraph("Place : ", small_normal));
            BI_Pdftable_tcc_place.setVerticalAlignment(Element.ALIGN_CENTER);
            PdfPCell BI_Pdftable_tcc_place_val = new PdfPCell(new Paragraph(edt_tcc_query_place.getText().toString(), small_normal));
            BI_Pdftable_tcc_place_val.setVerticalAlignment(Element.ALIGN_CENTER);
            PdfPCell BI_Pdftable_tcc_date = new PdfPCell(new Paragraph("Date : ", small_normal));
            BI_Pdftable_tcc_date.setVerticalAlignment(Element.ALIGN_CENTER);
            PdfPCell BI_Pdftable_tcc_date_val = new PdfPCell(new Paragraph(txt_tcc_query_date.getText().toString(), small_normal));
            BI_Pdftable_tcc_date_val.setVerticalAlignment(Element.ALIGN_CENTER);


            PdfPCell BI_Pdftable_tcc_sign1 = new PdfPCell(new Paragraph("Customer Singature : ", small_normal));
            BI_Pdftable_tcc_sign1.setVerticalAlignment(Element.ALIGN_CENTER);

            byte[] fbyt_Proposer = Base64.decode(str_customer_sign, 0);
            Bitmap customerbitmap = BitmapFactory.decodeByteArray(fbyt_Proposer, 0, fbyt_Proposer.length);
            // PdfPCell BI_PdftablePolicyHolder_signature_3 = new
            // PdfPCell();
            // BI_PdftablePolicyHolder_signature_3.setFixedHeight(60f);
            ByteArrayOutputStream PolicyHolder_signature_stream = new ByteArrayOutputStream();

            (customerbitmap).compress(Bitmap.CompressFormat.PNG, 50, PolicyHolder_signature_stream);
            Image PolicyHolder_signature = Image.getInstance(PolicyHolder_signature_stream.toByteArray());
            PolicyHolder_signature.scaleToFit(90, 90);
            PolicyHolder_signature.setBorder(Rectangle.BOX);

            PdfPCell BI_Pdftable_tcc_sign1_val = new PdfPCell(PolicyHolder_signature);
            BI_Pdftable_tcc_sign1_val.setVerticalAlignment(Element.ALIGN_CENTER);

            BI_Pdftable_tcc_sign.addCell(BI_Pdftable_tcc_place);
            BI_Pdftable_tcc_sign.addCell(BI_Pdftable_tcc_place_val);
            BI_Pdftable_tcc_sign.addCell(BI_Pdftable_tcc_date);
            BI_Pdftable_tcc_sign.addCell(BI_Pdftable_tcc_date_val);
            BI_Pdftable_tcc_sign.addCell(BI_Pdftable_tcc_sign1);
            BI_Pdftable_tcc_sign.addCell(BI_Pdftable_tcc_sign1_val);
            document.add(BI_Pdftable_tcc_sign);*/

            document.add(para_img_logo_after_space_1);

            document.close();

        } catch (Exception e) {

            isError = false;

            mCommonMethods.showToast(mContext, e.toString() + "Error in creating pdf");
        }

        return isError;
    }

    class AsynchValidateURN extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private ParseXML mParse;

        @Override
        protected void onPreExecute() {

            mProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Loading Please wait...";
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try{

                running = true;

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_SCORE_CARD_VALIDATE_URN);
                request.addProperty("strURN", str_urn_no);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(ServiceURL.SERVICE_URL);
                try {

                    androidHttpTranport.call(NAMESPACE + METHOD_NAME_SCORE_CARD_VALIDATE_URN, envelope);
                    //Object response = envelope.getResponse();

                    SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();

                    String str_res = sa.toString();
                    mParse = new ParseXML();

                    str_res = mParse.parseXmlTag(str_res, "NewDataSet");
                    if (str_res != null){

                        List<String> mData = mParse.parseParentNode(str_res, "Table");

                        if (mData.size() > 0){
                            /*for (String strXMl: mData){
                                if (mData.indexOf(strXMl) == 0){
                                    str_exam_location = mParse.parseXmlTag(strXMl, "EXAM_CENTER_LOCATION");
                                }
                            }*/
                            return "1";
                        }else {
                            return "0";
                        }
                    }else{
                        return "0";
                    }
                } catch (Exception e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    running = false;
                }
            } catch (Exception e) {
                try {
                    throw (e);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                running = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running){
                if (!s.equals("0")){

                    rl_score_card_questions.setVisibility(View.VISIBLE);

                }else{
                    rl_score_card_questions.setVisibility(View.GONE);
                    mCommonMethods.showMessageDialog(mContext, "Invalid URN");
                }
            }else{
                rl_score_card_questions.setVisibility(View.GONE);
                mCommonMethods.showMessageDialog(mContext, mCommonMethods.WEEK_INTERNET_MESSAGE);
            }
        }
    }
}
