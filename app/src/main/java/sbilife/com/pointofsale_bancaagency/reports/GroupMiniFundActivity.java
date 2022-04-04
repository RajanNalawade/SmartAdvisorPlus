package sbilife.com.pointofsale_bancaagency.reports;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;

public class GroupMiniFundActivity extends AppCompatActivity implements View.OnClickListener, ServiceHits.DownLoadData{

    private Context mContext;
    private CommonMethods mCommonMethods;

    private  final String NAMESPACE = "http://tempuri.org/";
    private  final String URl = ServiceURL.SERVICE_URL;

    private  final String METHOD_NAME_GROUP_MINI_FUND_STATEMENT = "getFundStatement";

    private String SOAP_ACTION_SH = "http://tempuri.org/saveSmartAdvisorServiceHits";
    private String METHOD_NAME_SH = "saveSmartAdvisorServiceHits";

    private final int DIALOG_DOWNLOAD_PROGRESS = 0;

    private EditText edt_mini_fund_statement_policy_no;
    private TextView txt_mini_fund_statement_error;

    private String str_policy_no = "",strCIFBDMUserId = "", strCIFBDMEmailId= "", strCIFBDMPassword= "",
            strCIFBDMMObileNo= "";
    private ArrayList<ParseXML.XMLHolderGroupMiniFundStatement> lstGroupTermCDStatement = new ArrayList<ParseXML.XMLHolderGroupMiniFundStatement>();

    private ProgressDialog mProgressDialog;

    private DownloadFileAsyncGroupMiniFundStatement taskMiniFundStatement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_group_mini_fund);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        initialisation();
        getUserDetails();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btn_mini_fund_statement_ok:
                getMiniFundStatementDetails();
                break;

            default:
                break;
        }
    }

    private void initialisation(){
        mContext = this;
        mCommonMethods = new CommonMethods();
        mCommonMethods.setApplicationToolbarMenu(this,"Fund Mini Fund Statement"); ;

        edt_mini_fund_statement_policy_no = findViewById(R.id.edt_mini_fund_statement_policy_no);
        Button btn_mini_fund_statement_ok = findViewById(R.id.btn_mini_fund_statement_ok);
        txt_mini_fund_statement_error = findViewById(R.id.txt_mini_fund_statement_error);

        btn_mini_fund_statement_ok.setOnClickListener(this);

        mProgressDialog = new ProgressDialog(this);

        taskMiniFundStatement = new DownloadFileAsyncGroupMiniFundStatement();
    }

    private void getMiniFundStatementDetails(){
        str_policy_no = edt_mini_fund_statement_policy_no.getText().toString().trim();

        txt_mini_fund_statement_error.setVisibility(View.GONE);

        if (!str_policy_no.equals("")){
            ServiceHits service = new ServiceHits(mContext,METHOD_NAME_GROUP_MINI_FUND_STATEMENT, str_policy_no,
                    strCIFBDMUserId, strCIFBDMEmailId, strCIFBDMMObileNo,
                    strCIFBDMPassword, this);
            service.execute();
        }else{
            mCommonMethods.showToast(mContext, "Please Enter Policy Number");
        }
    }

    private void getUserDetails() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = mCommonMethods
                .setUserDetails(mContext);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();

        mCommonMethods.printLog("user details", "strCIFBDMUserId:" + strCIFBDMUserId
                + " strCIFBDMEmailId:" + strCIFBDMEmailId
                + " strCIFBDMPassword:" + strCIFBDMPassword
                + " strCIFBDMMObileNo:" + strCIFBDMMObileNo);
    }

    class DownloadFileAsyncGroupMiniFundStatement extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String inputpolicylist = "", strErrorCode = "", strErrorMsg = "";

        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {

                running = true;
                SoapObject request = null;

                request = new SoapObject(NAMESPACE,METHOD_NAME_GROUP_MINI_FUND_STATEMENT);
                request.addProperty("strPolNo", str_policy_no);
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

                    String SOAP_ACTION_GROUP_MINI_FUND_STATEMENT = "http://tempuri.org/getFundStatement";
                    androidHttpTranport.call(
                            SOAP_ACTION_GROUP_MINI_FUND_STATEMENT, envelope);

                    SoapPrimitive sa = null;
                    try {
                        sa = (SoapPrimitive) envelope.getResponse();

                        inputpolicylist = sa.toString();

                        ParseXML prsObj = new ParseXML();

                        inputpolicylist = prsObj.parseXmlTag(
                                inputpolicylist, "CIFPolicyList");

                        inputpolicylist = prsObj.parseXmlTag(
                                inputpolicylist, "ScreenData");

                        if (inputpolicylist == null){
                            inputpolicylist = sa.toString();

                            inputpolicylist = prsObj.parseXmlTag(
                                    inputpolicylist, "CIFPolicyList");

                            List<String> Node = prsObj.parseParentNode(
                                    inputpolicylist, "Table");
                            List<ParseXML.XMLHolderGroupMiniFundStatement> nodeData = prsObj
                                    .parseNodeGroupMiniFundStatement(Node);

                            // final List<XMLHolderMaturity> lst;
                            lstGroupTermCDStatement = new ArrayList<ParseXML.XMLHolderGroupMiniFundStatement>();
                            lstGroupTermCDStatement.clear();

                            for (ParseXML.XMLHolderGroupMiniFundStatement node : nodeData) {

                                lstGroupTermCDStatement.add(node);
                            }
                        }else{
                            strErrorCode = prsObj.parseXmlTag(
                                    inputpolicylist, "ErrCode");

                            strErrorMsg = prsObj.parseXmlTag(
                                    inputpolicylist, "ErrorMsg");
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

                } catch (IOException e) {
                    try {
                        throw (e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    mProgressDialog.dismiss();
                    running = false;
                } catch (XmlPullParserException e) {
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
        protected void onPostExecute(String s) {
            if (mProgressDialog.isShowing()) {
                dismissProgressDialog();
            }

            if (running) {
                if (!strErrorCode.equals("1")){

                    createMiniFundStatementPDF();

                }else{
                    txt_mini_fund_statement_error.setVisibility(View.VISIBLE);
                    txt_mini_fund_statement_error.setText(strErrorMsg);
                }
            }else{
                mCommonMethods.showMessageDialog(mContext, mCommonMethods.SERVER_ERROR);
            }
        }
    }

    private void showProgressDialog() {
        showDialog(DIALOG_DOWNLOAD_PROGRESS);
    }

    private void dismissProgressDialog() {
        dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(this,
                        ProgressDialog.THEME_HOLO_LIGHT);
                String Message = "Loading Please wait...";
                mProgressDialog.setMessage(Html
                        .fromHtml("<font color='#00a1e3'><b>" + Message
                                + "<b></font>"));
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setCancelable(false);

                mProgressDialog.setButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                //taskPolicyList.cancel(true);

                                if(taskMiniFundStatement!=null){
                                    taskMiniFundStatement.cancel(true);
                                }
                                mProgressDialog.dismiss();
                            }
                        });

                mProgressDialog.setMax(100);
                mProgressDialog.show();
                return mProgressDialog;

            default:
                return null;
        }
    }

    @Override
    public void downLoadData() {
        taskMiniFundStatement = new DownloadFileAsyncGroupMiniFundStatement();
        taskMiniFundStatement.execute();
    }

    private void createMiniFundStatementPDF() {
        try {

            Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 11,
                    Font.BOLD, BaseColor.WHITE);
            Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.BOLD);

            Font small_bold1 = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.BOLD);

            Font small_bold2 = new Font(Font.FontFamily.TIMES_ROMAN, 4,
                    Font.BOLD);

            Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.NORMAL);
            Font small_bold_for_name = new Font(Font.FontFamily.TIMES_ROMAN,
                    10, Font.BOLD);

            // File mypath = new File(folder, PropserNumber +
            // "Proposalno_p02.pdf");
            File mypath = new StorageUtils().createFileToAppSpecificDir(
                    mContext, str_policy_no + "MiniFundStatement.pdf");

            Rectangle rect = new Rectangle(594f, 792f);

            Document document = new Document(rect, 50, 50, 50, 50);
            // Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            @SuppressWarnings("unused")
            PdfWriter pdf_writer = null;
            pdf_writer = PdfWriter.getInstance(document, new FileOutputStream(
                    mypath.getAbsolutePath()));

            document.open();

            // For SBI- Life Logo starts
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext()
                    .getResources(), R.drawable.sbi_life_logo);
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
            Image img_sbi_logo = Image.getInstance(stream.toByteArray());
            img_sbi_logo.setAlignment(Image.LEFT);
            img_sbi_logo.getSpacingAfter();
            img_sbi_logo.scaleToFit(80, 50);

            Paragraph para_img_logo = new Paragraph("");
            para_img_logo.add(img_sbi_logo);

            Paragraph para_img_logo_after_space_1 = new Paragraph(" ");

            document.add(para_img_logo);
            // For SBI- Life Logo ends

            // To draw line after the sbi logo image
            document.add(new LineSeparator());
            document.add(para_img_logo_after_space_1);

            // For the BI Smart Elite Table Header(Grey One)
            Paragraph Para_Header = new Paragraph();
            Para_Header
                    .add(new Paragraph(
                            "Mini Fund Statement of " + str_policy_no,
                            headerBold));

            PdfPTable headertable = new PdfPTable(1);
            headertable.setWidthPercentage(100);
            PdfPCell c1 = new PdfPCell(new Phrase(Para_Header));
            c1.setBackgroundColor(BaseColor.DARK_GRAY);
            c1.setPadding(5);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            headertable.addCell(c1);
            headertable.setHorizontalAlignment(Element.ALIGN_CENTER);
            Paragraph para_address = new Paragraph(
                    "SBI Life Insurance Co. Ltd Corporate Office: 'Natraj', M.V. Road and Western Express, Highway Junction, Andheri (East),Mumbai � 400069. Regn No. 111",
                    small_bold_for_name);
            para_address.setAlignment(Element.ALIGN_CENTER);
            Paragraph para_address1 = new Paragraph(
                    "Website: www.sbilife.co.in | Email: info@sbilife.co.in | CIN: L99999MH2000PLC129113. Toll Free: 1800 267 9090 (Between 9.00 am & 9.00 pm)",
                    small_bold);
            para_address1.setAlignment(Element.ALIGN_CENTER);
            document.add(para_address);
            document.add(para_address1);
            document.add(para_img_logo_after_space_1);
            document.add(headertable);
            document.add(para_img_logo_after_space_1);

            document.add(para_img_logo_after_space_1);

            PdfPTable BI_PdftableCDStatement = new PdfPTable(5);
            BI_PdftableCDStatement.setWidthPercentage(100);

            PdfPCell BI_PdftableCDStatement_cell1 = new PdfPCell(new Paragraph(
                    "Policy No.", small_bold1));

            PdfPCell BI_PdftableCDStatement_cell2 = new PdfPCell(new Paragraph(
                    "Billing Date", small_bold1));

            PdfPCell BI_PdftableCDStatement_cell3 = new PdfPCell(new Paragraph(
                    "Description", small_bold1));

            PdfPCell BI_PdftableCDStatement_cell4 = new PdfPCell(new Paragraph(
                    "Transaction Amount", small_bold1));

            PdfPCell BI_PdftableCDStatement_cell5 = new PdfPCell(new Paragraph(
                    "Balance", small_bold1));

            BI_PdftableCDStatement_cell1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableCDStatement_cell1.setPadding(5);

            BI_PdftableCDStatement_cell2
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableCDStatement_cell2.setPadding(5);

            BI_PdftableCDStatement_cell3
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableCDStatement_cell3.setPadding(5);

            BI_PdftableCDStatement_cell4
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableCDStatement_cell4.setPadding(5);

            BI_PdftableCDStatement_cell5
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            BI_PdftableCDStatement_cell5.setPadding(5);

            BI_PdftableCDStatement.addCell(BI_PdftableCDStatement_cell1);
            BI_PdftableCDStatement.addCell(BI_PdftableCDStatement_cell2);
            BI_PdftableCDStatement.addCell(BI_PdftableCDStatement_cell3);
            BI_PdftableCDStatement.addCell(BI_PdftableCDStatement_cell4);
            BI_PdftableCDStatement.addCell(BI_PdftableCDStatement_cell5);
            document.add(BI_PdftableCDStatement);

            for (ParseXML.XMLHolderGroupMiniFundStatement node : lstGroupTermCDStatement) {

                PdfPTable miniFundStatement_row = new PdfPTable(5);
                miniFundStatement_row.setWidthPercentage(100);

                PdfPCell CDStatement_row_cell1 = new PdfPCell(
                        new Paragraph(node.getPolicy_no(),
                                small_bold2));

                PdfPCell CDStatement_row_cell2 = new PdfPCell(
                        new Paragraph(node.getBiling_date(),
                                small_bold2));

                PdfPCell CDStatement_row_cell3 = new PdfPCell(
                        new Paragraph(node.getDescription(),
                                small_bold2));

                PdfPCell CDStatement_row_cell4 = new PdfPCell(
                        new Paragraph(node.getTransaction_amount(),
                                small_bold2));

                PdfPCell CDStatement_row_cell5 = new PdfPCell(
                        new Paragraph(node.getBalance(),
                                small_bold2));

                miniFundStatement_row.addCell(CDStatement_row_cell1);
                miniFundStatement_row.addCell(CDStatement_row_cell2);
                miniFundStatement_row.addCell(CDStatement_row_cell3);
                miniFundStatement_row.addCell(CDStatement_row_cell4);
                miniFundStatement_row.addCell(CDStatement_row_cell5);

                document.add(miniFundStatement_row);
            }

            document.close();

            mCommonMethods.openPDFAction(mypath, mContext);

        } catch (Exception e) {
            Log.i(getLocalClassName(), e.toString() + "Error in creating pdf");
        }
    }
}
