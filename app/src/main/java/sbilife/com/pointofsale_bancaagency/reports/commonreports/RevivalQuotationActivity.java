package sbilife.com.pointofsale_bancaagency.reports.commonreports;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.reports.ServiceHits;
import sbilife.com.pointofsale_bancaagency.utility.GridHeight;

public class RevivalQuotationActivity extends AppCompatActivity implements ServiceHits.DownLoadData, View.OnClickListener {

    private final String METHOD_NAME_REVIVAL_QUOTATION = "getRevival_Quotation";
    //Input
    private CommonMethods commonMethods;
    private StorageUtils mStorageUtils;
    private Context context;
    private ProgressDialog mProgressDialog;
    private String policyNumber = "";
    private String paidByDate = "";
    private String dueAmount = "";
    private ServiceHits service;
    private DownloadRevialQuoTationAsync downloadRevialQuoTationAsync;
    private LinearLayout linearlayoutSendSMSEmail;
    private String strFileName = "",mobileNumber = "",emailId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_revival_quotation);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        context = this;
        commonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
        commonMethods.setApplicationToolbarMenu(this, "Revival Quotation");
        Intent intent = getIntent();
        Button btnSendEmail, btnSavePDF;
        policyNumber = intent.getStringExtra("policyNumber");
        mobileNumber = intent.getStringExtra("mobileNumber");
        emailId= intent.getStringExtra("emailId");


        btnSendEmail = findViewById(R.id.btnSendEmail);
        btnSavePDF = findViewById(R.id.btnSavePDF);
        btnSendEmail.setOnClickListener(this);
        btnSavePDF.setOnClickListener(this);

        linearlayoutSendSMSEmail = findViewById(R.id.linearlayoutSendSMSEmail);
        linearlayoutSendSMSEmail.setVisibility(View.GONE);

        service_hits();
    }

    private void service_hits() {
        String strCIFBDMUserId, strCIFBDMEmailId,
                strCIFBDMPassword, strCIFBDMMObileNo;
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods
                .setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();

        service = new ServiceHits(context,
                METHOD_NAME_REVIVAL_QUOTATION, policyNumber,
                strCIFBDMUserId, strCIFBDMEmailId, strCIFBDMMObileNo,
                strCIFBDMPassword, this);
        service.execute();
    }

    @Override
    public void onClick(View view) {
        String body = "Revival Quotation for Policy Number - "+policyNumber+"\n If Paid By - "+ paidByDate
                + ", Amount - " +dueAmount
                +"\n To Pay click\n "
                +"https://mypolicy.sbilife.co.in/Campaign/RevivalQuotation.aspx?src=website";

        switch (view.getId()) {

            case R.id.btnSendEmail:
                String []addresses = {emailId};
                composeEmail(addresses,"Revival Quotation ",body);
                break;
            case R.id.btnSavePDF:
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"+mobileNumber));
                    intent.putExtra("sms_body", body);
                    startActivity(intent);
                }catch(Exception e){
                    e.printStackTrace();
                }
                break;
        }
    }

    private void composeEmail(String[] addresses, String subject, String body) {
        try {

            String title = "Open with";
           /* Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, addresses);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, body);
            Intent chooser = Intent.createChooser(intent, title);

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(chooser);
            }*/


            File filelocation = mStorageUtils.createFileToAppSpecificDir(context, strFileName);
            Uri path = Uri.fromFile(filelocation);
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            // set the type to 'email'
            emailIntent .setType("vnd.android.cursor.dir/email");
            emailIntent .putExtra(Intent.EXTRA_EMAIL, addresses);
            // the attachment
            emailIntent .putExtra(Intent.EXTRA_STREAM, path);
            //emailIntent.setType("*/*");
            // emailIntent.setType("text/plain");
            //emailIntent.setType("application/pdf");
            // the mail subject
            emailIntent .putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, body);
            Intent chooser = Intent.createChooser(emailIntent, title);

            if (emailIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(chooser);
            }


        } catch (SecurityException e) {
            commonMethods.showMessageDialog(context,"There might be issue in Permissions");
        }catch(Exception e) {
            commonMethods.showMessageDialog(context,"Something went wrong");
        }
    }

    @Override
    public void downLoadData() {
        downloadRevialQuoTationAsync = new DownloadRevialQuoTationAsync();
        downloadRevialQuoTationAsync.execute();
    }

    class DownloadRevialQuoTationAsync extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String result;
        private List<String> Node1, node2;
        private TextView txtpolicyno, txtpolicyholdername, txtdateofcommence,
                txtsumassured, txtplan, txtpolicyterm, txtDOB, txtage,
                txtpremium_not_paid, txtquotationdate, txtfreq,
                txtphysicalextra, txtinstPrem, txtEMRextra, txtCritiIllness;
        private LinearLayout ll_grid_quotation_schedule, ll_grid_breakup_dues;
        private GridView gv_quotation_schedule, gv_breakup_dues;
        private List<HashMap<String, String>> lstBreakUpDues;
        private List<ParseXML.XMLQuotationSchedule> list_data;
        private String strPolicyNo, strPolicyHolderFirstName, strPolicyHolderMiddleName,
                strPolicyHolderLastName, strDateOfCommentcment,
                strSumAssured, strPlan, strPolicyTerm, strDOB,
                strQuotationDate, strAge, strPremium_not_paid,
                strFrequency, strPhysicalExtra, strEMR_Extra,
                strCriticalIllness, strInstmentPrem;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            txtpolicyno = findViewById(R.id.txtpolicyno);
            txtpolicyholdername = findViewById(R.id.txtpolicyholdername);
            txtdateofcommence = findViewById(R.id.txtdateofcommence);
            txtsumassured = findViewById(R.id.txtsumassured);
            txtplan = findViewById(R.id.txtplan);
            txtpolicyterm = findViewById(R.id.txtpolicyterm);
            txtDOB = findViewById(R.id.txtDOB);
            txtage = findViewById(R.id.txtage);
            txtpremium_not_paid = findViewById(R.id.txtpremium_not_paid);
            txtquotationdate = findViewById(R.id.txtquotationdate);
            txtfreq = findViewById(R.id.txtfreq);
            txtphysicalextra = findViewById(R.id.txtphysicalextra);
            txtinstPrem = findViewById(R.id.txtinstPrem);
            txtEMRextra = findViewById(R.id.txtEMRextra);
            txtCritiIllness = findViewById(R.id.txtCritiIllness);
            ll_grid_quotation_schedule = findViewById(R.id.ll_grid_quotation_schedule);
            ll_grid_breakup_dues = findViewById(R.id.ll_grid_breakup_dues);
            gv_quotation_schedule = findViewById(R.id.gv_quotation_schedule);
            gv_breakup_dues = findViewById(R.id.gv_breakup_dues);

            mProgressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Loading Please wait...";
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(true);

            mProgressDialog.setButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            if (downloadRevialQuoTationAsync != null) {
                                downloadRevialQuoTationAsync.cancel(true);
                            }
                            if (mProgressDialog != null) {
                                if (mProgressDialog.isShowing()) {
                                    mProgressDialog.dismiss();
                                }
                            }
                        }
                    });

            mProgressDialog.setMax(100);
            mProgressDialog.show();
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request;
                String NAMESPACE = "http://tempuri.org/";
                request = new SoapObject(NAMESPACE, METHOD_NAME_REVIVAL_QUOTATION);
                request.addProperty("strPolicyNo", policyNumber);//commonMethods.GetUserCode(context));


                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                commonMethods.TLSv12Enable();
                String URl = ServiceURL.SERVICE_URL;
                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {
                    String SOAP_ACTION_REVIVAL_QUOTATION = "http://tempuri.org/getRevival_Quotation";
                    androidHttpTranport.call(SOAP_ACTION_REVIVAL_QUOTATION, envelope);

                    ParseXML prsObj = new ParseXML();
                    Object response = envelope.getResponse();
                    result = response.toString();
                    result = prsObj.parseXmlTag(result, "NewDataSet");
                    String errorCode = prsObj.parseXmlTag(result, "ErrorCode");
                    String errormsg = prsObj.parseXmlTag(result, "ErrorDescription");
                    System.out.println("Revival result " + result);

                    if (errorCode == null && errormsg == null && !result.equals("")) {
                        strPolicyNo = prsObj.parseXmlTag(result, "pl_polno_s");
                        strPolicyNo = strPolicyNo == null ? "" : strPolicyNo;

                        strPolicyHolderFirstName = prsObj.parseXmlTag(result, "pd_first_name");
                        strPolicyHolderFirstName = strPolicyHolderFirstName == null ? "" : strPolicyHolderFirstName;


                        strPolicyHolderMiddleName = prsObj.parseXmlTag(result, "pd_middle_name");
                        strPolicyHolderMiddleName = strPolicyHolderMiddleName == null ? "" : strPolicyHolderMiddleName;

                        strPolicyHolderLastName = prsObj.parseXmlTag(result, "pd_last_name");
                        strPolicyHolderLastName = strPolicyHolderLastName == null ? "" : strPolicyHolderLastName;

                        strDateOfCommentcment = prsObj.parseXmlTag(result, "pl_commencement_date");
                        strDateOfCommentcment = strDateOfCommentcment == null ? "" : strDateOfCommentcment;

                        strSumAssured = prsObj.parseXmlTag(result, "pl_sumnp");
                        strSumAssured = strSumAssured == null ? "" : strSumAssured;

                        strPlan = prsObj.parseXmlTag(result, "pl_product_s");
                        strPlan = strPlan == null ? "" : strPlan;

                        strPolicyTerm = prsObj.parseXmlTag(result, "pl_term_n");
                        strPolicyTerm = strPolicyTerm == null ? "" : strPolicyTerm;

                        strDOB = prsObj.parseXmlTag(result, "pd_dob");
                        strDOB = strDOB == null ? "" : strDOB;

                        strAge = prsObj.parseXmlTag(result, "pd_age");
                        strAge = strAge == null ? "" : strAge;

                        strPremium_not_paid = prsObj.parseXmlTag(result, "pl_fup_dt");
                        strPremium_not_paid = strPremium_not_paid == null ? "" : strPremium_not_paid;

                        strQuotationDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                        strQuotationDate = strQuotationDate == null ? "" : strQuotationDate;

                        strFrequency = prsObj.parseXmlTag(result, "pl_mode");
                        strFrequency = strFrequency == null ? "" : strFrequency;

                        strPhysicalExtra = prsObj.parseXmlTag(result, "pl_physical_extra");
                        strPhysicalExtra = strPhysicalExtra == null ? "" : strPhysicalExtra;

                        strInstmentPrem = prsObj.parseXmlTag(result, "pl_inst_premium");
                        strInstmentPrem = strInstmentPrem == null ? "" : strInstmentPrem;

                        strEMR_Extra = prsObj.parseXmlTag(result, "pl_emr_extra");
                        strEMR_Extra = strEMR_Extra == null ? "" : strEMR_Extra;

                        strCriticalIllness = prsObj.parseXmlTag(result, "pl_ci_extra");
                        strCriticalIllness = strCriticalIllness == null ? "" : strCriticalIllness;

                        Node1 = prsObj.parseParentNode(result, "QuotationSchedule");
                        list_data = prsObj.parseNodeElementQuotationSchedule(Node1);

                        node2 = prsObj.parseParentNode(result, "RevivalDues");
                        lstBreakUpDues = prsObj.parseNodeElementBreakupDues(node2);
                        createPdf();
                        return "Success";
                    } else
                        return errormsg;

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
        protected void onProgressUpdate(String... progress) {
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (running) {
                if (unused.equalsIgnoreCase("Success")) {
                    txtpolicyno.setText(strPolicyNo.trim());
                    txtpolicyholdername.setText(strPolicyHolderFirstName.trim() + " " + strPolicyHolderMiddleName + " " + strPolicyHolderLastName);
                    txtdateofcommence.setText(strDateOfCommentcment.trim());
                    txtsumassured.setText(strSumAssured.trim());
                    txtplan.setText(strPlan.trim());
                    txtpolicyterm.setText(strPolicyTerm.trim());
                    txtDOB.setText(strDOB.trim());
                    txtage.setText(strAge.trim());
                    txtpremium_not_paid.setText(strPremium_not_paid.trim());
                    txtquotationdate.setText(strQuotationDate);
                    txtfreq.setText(strFrequency.trim());
                    txtphysicalextra.setText(strPhysicalExtra.trim());
                    txtinstPrem.setText(strInstmentPrem.trim());
                    txtEMRextra.setText(strEMR_Extra.trim());
                    txtCritiIllness.setText(strCriticalIllness.trim());


                    if (list_data.size() > 0) {
                        linearlayoutSendSMSEmail.setVisibility(View.VISIBLE);
                        ll_grid_quotation_schedule.setVisibility(View.VISIBLE);
                        gv_quotation_schedule.setVerticalScrollBarEnabled(true);
                        gv_quotation_schedule.setSmoothScrollbarEnabled(true);
                        //gv_quotation_schedule.getLayoutParams().height = 70 * (list_data.size() + 1);

                        Adapter_BI_QuotationScheduleGrid adapter = new Adapter_BI_QuotationScheduleGrid(list_data);
                        gv_quotation_schedule.setAdapter(adapter);
                        GridHeight gh =new GridHeight();

                        gh.getheight(gv_quotation_schedule,String.valueOf(list_data.size()));

                        String latestDueDate = list_data.get(0).getLatest_due();
                        paidByDate = list_data.get(0).getIf_paid_by();
                        dueAmount = list_data.get(0).getNet_revival_amount();
                    } else {
                        linearlayoutSendSMSEmail.setVisibility(View.GONE);
                        ll_grid_quotation_schedule.setVisibility(View.GONE);
                    }

                    if (lstBreakUpDues.size() > 0) {
                        ll_grid_breakup_dues.setVisibility(View.VISIBLE);
                        gv_breakup_dues.setVerticalScrollBarEnabled(true);
                        gv_breakup_dues.setSmoothScrollbarEnabled(true);
                        // gv_breakup_dues.getLayoutParams().height = 70 * (lstBreakUpDues.size() + 1);
                        Adapter_BI_BreakupDues adapter = new Adapter_BI_BreakupDues(lstBreakUpDues);
                        gv_breakup_dues.setAdapter(adapter);
                        GridHeight gh =new GridHeight();

                        gh.getheight(gv_breakup_dues,String.valueOf(lstBreakUpDues.size()));
                    } else {
                        ll_grid_breakup_dues.setVisibility(View.GONE);
                    }

                } else {
                    commonMethods.showMessageDialog(context, unused);
                }
            } else {
                commonMethods.showMessageDialog(context,
                        "Server Not Responding,Try again..");
            }
        }

        void createPdf() {
            try {

                Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.NORMAL);
                Font normal_italic = new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.ITALIC);
                Font normal_bold = new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD);
                //				Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.NORMAL);
                Font normal_bold_underline = new Font(Font.FontFamily.TIMES_ROMAN, 16,Font.BOLD | Font.UNDERLINE);
                //				Font footer_normal = new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL);

                strFileName = "Revival_Quotation_"+strPolicyNo+".pdf";

                File mypath = mStorageUtils.createFileToAppSpecificDir(context, strFileName);

                if (mypath.exists()) {
                    mypath.delete();
                }

                DecimalFormat currencyFormat = new DecimalFormat("##,##,##,###.##");

                Document document = new Document(PageSize.A4, 40, 40, 40, 40);
                PdfWriter pdf_writer = null;
                pdf_writer = PdfWriter.getInstance(document, new FileOutputStream(mypath.getAbsolutePath()));
                //pdf_writer.setPageEvent(new HeaderAndFooter());

                // float[] columnWidths_4 = { 2f, 1f, 2f, 1f };
                Calendar c = Calendar.getInstance();
                document.open();

                PdfPTable table;
                Paragraph newline_para = new Paragraph("\n");

                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                        R.drawable.sbi_life_logo);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bitMapData = stream.toByteArray();
                Image image = Image.getInstance(bitMapData);
                image.scalePercent(50f);
                image.scaleToFit(80, 50);
                image.setAlignment(Element.ALIGN_LEFT);

                PdfPTable table1 = new PdfPTable(1);
                table1.setWidths(new float []{2f});
                table1.setWidthPercentage(100);
                PdfPCell cell;
                Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                        Font.BOLD);

                cell = new PdfPCell(new Phrase("SBI Life Insurance Company Limited",normal_bold_underline));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                table1.addCell(cell);

                cell = new PdfPCell(image);
                cell.setRowspan(3);
                cell.setBorder(Rectangle.NO_BORDER);
                table1.addCell(cell);

                document.add(table1);

                PdfPTable table2 = new PdfPTable(1);
                table2.setWidths(new float []{2f});
                table2.setWidthPercentage(100);

                cell = new PdfPCell(new Phrase("Date: " +new SimpleDateFormat("dd-MM-yyyy").format(c.getTime()),small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell);

                cell = new PdfPCell(new Phrase(strPolicyHolderFirstName.trim()+" "+strPolicyHolderMiddleName+" "+strPolicyHolderLastName,normal_bold));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell);


                cell = new PdfPCell(newline_para);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell);

                cell = new PdfPCell(new Phrase("Dear Customer,",small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell);

                cell = new PdfPCell(newline_para);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell);

                cell = new PdfPCell(new Phrase("Ref: Policy No: "+strPolicyNo,small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell);
                cell = new PdfPCell(newline_para);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell);
                document.add(table2);


                table2 = new PdfPTable(1);
                table2.setWidths(new float []{2f});
                table2.setWidthPercentage(100);
                cell = new PdfPCell(new Phrase("This is with reference to your request for the revival of the above mentioned policy. We would like to inform you that based on the current status of your policy account, we are pleased to enclose the revival quotation, as applicable.",small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell);
                document.add(table2);

                table2 = new PdfPTable(1);
                table2.setWidths(new float []{2f});
                table2.setWidthPercentage(100);
                cell = new PdfPCell(new Phrase("Revival Quotation: ",small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell);

                cell = new PdfPCell(new Phrase("Frequency              : "+strFrequency,small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell);

                cell = new PdfPCell(new Phrase("Installment Premium    : "+strInstmentPrem,small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell);

                cell = new PdfPCell(new Phrase("Premium not paid since : "+strPremium_not_paid,small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell);

                document.add(table2);
                document.add(newline_para);

                table1 = new PdfPTable(10);
                table1.setWidths(new float []{2f,2f,2f,2f,2f,2f,2f,2f,2f,2f});
                table1.setWidthPercentage(100);
                cell = new PdfPCell(new Phrase("If Paid By\n", normal_bold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);
                cell = new PdfPCell(new Phrase("Latest Due", normal_bold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);
                cell = new PdfPCell(new Phrase("No of Premiums", normal_bold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Total Amount (A)", normal_bold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Rate of Interest(%)", normal_bold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Late Fee (B)", normal_bold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                cell = new PdfPCell(new Phrase("Interest Waived(C)", normal_bold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);

                /* Added by Priyanka Warekar - 14-03-2017 -start ******/
                cell = new PdfPCell(new Phrase("Other Due Pending on the Policy(D)", normal_bold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);
                /* Added by Priyanka Warekar - 14-03-2017 -end ******/
                cell = new PdfPCell(new Phrase("Policy Deposit (E)", normal_bold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);
                cell = new PdfPCell(new Phrase("Net Revival Amount\n" +
                        "((A+B+D)-(C+E))\n", normal_bold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table1.addCell(cell);
                for (int i = 0; i < list_data.size(); i++) {
                    cell = new PdfPCell(new Phrase(list_data.get(i).getIf_paid_by(), small_normal));
                    table1.addCell(cell);

                    cell = new PdfPCell(new Phrase(list_data.get(i).getLatest_due(), small_normal));
                    table1.addCell(cell);

                    cell = new PdfPCell(new Phrase(list_data.get(i).getNo_of_premium(), small_normal));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table1.addCell(cell);

                    cell = new PdfPCell(new Phrase(currencyFormat.format(Double.parseDouble(list_data.get(i).getTotal_amount())), small_normal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);

                    cell = new PdfPCell(new Phrase(list_data.get(i).getRate_of_interest(), small_normal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);

                    cell = new PdfPCell(new Phrase(currencyFormat.format(Double.parseDouble(list_data.get(i).getLate_fee())), small_normal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);

                    cell = new PdfPCell(new Phrase(currencyFormat.format(Double.parseDouble(list_data.get(i).getInterest_waived())), small_normal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);


                    /*Added by Priyanka Warekar - 14-03-2017 -start ******/
                    cell = new PdfPCell(new Phrase(currencyFormat.format(Double.parseDouble(list_data.get(i).getOther_dues_pending())), small_normal));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(cell);
                    /*Added by Priyanka Warekar - 14-03-2017 -end ******/

                    cell = new PdfPCell(new Phrase(currencyFormat.format(Double.parseDouble(list_data.get(i).getPolicy_deposit())), small_normal));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table1.addCell(cell);

                    cell = new PdfPCell(new Phrase(currencyFormat.format(Double.parseDouble(list_data.get(i).getNet_revival_amount())), small_normal));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table1.addCell(cell);
                }

                document.add(table1);


                table = new PdfPTable(1);
                table.setWidths(new float []{2f});
                table.setWidthPercentage(100);
                cell = new PdfPCell(new Phrase("(The Premium displayed in the quotation schedule is subject to change based on the underwriting decision and Issue of\n" +
                        "quotation doesn’t mean acceptance of any risk on the policy by us.)\n",normal_italic));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                document.add(table);
                document.add(newline_para);


                PdfPTable table4 = new PdfPTable(1);
                table4.setWidths(new float []{2f});
                table4.setWidthPercentage(100);

                cell = new PdfPCell(new Phrase("Any further requirements if required for revival will be intimated to you.",small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table4.addCell(cell);
                document.add(table4);

                /*+"\n To Pay click\n "
                        +"https://mypolicy.sbilife.co.in/Campaign/RevivalQuotation.aspx?src=website";*/

                Paragraph paragraph = new Paragraph();
                paragraph.add(new Phrase("To Pay click \n"));
                normal_bold.setColor(BaseColor.BLUE);
                Anchor anchor = new Anchor(
                        "https://mypolicy.sbilife.co.in/Campaign/RevivalQuotation.aspx?src=website",normal_bold);
                anchor.setReference(
                        "https://mypolicy.sbilife.co.in/Campaign/RevivalQuotation.aspx?src=website");
                paragraph.add(anchor);
                document.add(paragraph);
                document.add(newline_para);

                PdfPTable table5 = new PdfPTable(1);
                table5.setWidths(new float []{2f});
                table5.setWidthPercentage(100);
                cell = new PdfPCell(new Phrase("You can also make your payments via Cheque / Draft in favour of 'SBI Life Insurance Co. Ltd. Policy no. '"+policyNumber,small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table5.addCell(cell);
                document.add(table5);

                document.add(newline_para);

                PdfPTable table7 = new PdfPTable(1);
                table7.setWidths(new float []{2f});
                table7.setWidthPercentage(100);
                cell = new PdfPCell(new Phrase("Do revert if you need further clarifications Or email us at info@sbilife.co.in",small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table7.addCell(cell);
                document.add(table7);

                document.add(newline_para);

                PdfPTable table8 = new PdfPTable(1);
                table8.setWidths(new float []{2f});
                table8.setWidthPercentage(100);
                cell = new PdfPCell(new Phrase("Assuring you of our best services at all times.",small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table8.addCell(cell);
                document.add(table8);

                document.add(newline_para);

                PdfPTable table9 = new PdfPTable(1);
                table9.setWidths(new float []{2f});
                table9.setWidthPercentage(100);
                cell = new PdfPCell(new Phrase("This is a computer generated statement and does not require any signature.",small_normal));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(Rectangle.NO_BORDER);
                table9.addCell(cell);
                document.add(table9);

                document.close();

            } catch (Exception e) {
                Log.e(getLocalClassName(), e.toString() + "Error in creating Pdf");
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);

            }
        }
    }

    class Adapter_BI_QuotationScheduleGrid extends BaseAdapter {

        class ViewHolder {
            TextView tv_adapter_if_paid_by;
            TextView tv_adapter_latest_due;
            TextView tv_adapter_no_of_premium;
            TextView tv_adapter_total_amount;
            TextView tv_adapter_rate_of_interest;
            TextView tv_adapter_late_fee;
            TextView tv_adapter_bi_interest_waived;
            TextView tv_adapter_other_dues_pending;
            TextView tv_adapter_bi_policy_deposit;
            TextView tv_adapter_net_revival_amount;

        }

        final List<ParseXML.XMLQuotationSchedule> allElementDetails;

        private final int[] colors = new int[]{Color.parseColor("#DCDBDB"),
                Color.parseColor("#E8E8E8")};

        Adapter_BI_QuotationScheduleGrid(List<ParseXML.XMLQuotationSchedule> results) {
            allElementDetails = results;
        }

        public int getCount() {
            return allElementDetails.size();
        }

        public Object getItem(int position) {
            return null;
        }


        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater mInflater = LayoutInflater.from(context);
                convertView = mInflater.inflate(R.layout.adapter_quotation_schedule,
                        null);
                holder = new ViewHolder();
                holder.tv_adapter_if_paid_by = convertView.findViewById(R.id.tv_adapter_if_paid_by);
                holder.tv_adapter_latest_due = convertView.findViewById(R.id.tv_adapter_latest_due);
                holder.tv_adapter_no_of_premium = convertView.findViewById(R.id.tv_adapter_no_of_premium);
                holder.tv_adapter_total_amount = convertView.findViewById(R.id.tv_adapter_total_amount);
                holder.tv_adapter_rate_of_interest = convertView.findViewById(R.id.tv_adapter_rate_of_interest);
                holder.tv_adapter_late_fee = convertView.findViewById(R.id.tv_adapter_late_fee);
                holder.tv_adapter_bi_interest_waived = convertView.findViewById(R.id.tv_adapter_bi_interest_waived);
                holder.tv_adapter_other_dues_pending = convertView.findViewById(R.id.tv_adapter_other_dues_pending);
                holder.tv_adapter_bi_policy_deposit = convertView.findViewById(R.id.tv_adapter_bi_policy_deposit);
                holder.tv_adapter_net_revival_amount = convertView.findViewById(R.id.tv_adapter_net_revival_amount);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tv_adapter_if_paid_by.setText(String.valueOf(allElementDetails.get(position).getIf_paid_by()));
            holder.tv_adapter_latest_due.setText(String.valueOf(allElementDetails.get(position).getLatest_due()));
            holder.tv_adapter_no_of_premium.setText(String.valueOf(allElementDetails.get(position).getNo_of_premium()));
            holder.tv_adapter_total_amount.setText(String.valueOf(allElementDetails.get(position).getTotal_amount()));
            holder.tv_adapter_rate_of_interest.setText(String.valueOf(allElementDetails.get(position).getRate_of_interest()));
            holder.tv_adapter_late_fee.setText(String.valueOf(allElementDetails.get(position).getLate_fee()));
            holder.tv_adapter_bi_interest_waived.setText(String.valueOf(allElementDetails.get(position).getInterest_waived()));
            holder.tv_adapter_other_dues_pending.setText(String.valueOf(allElementDetails.get(position).getOther_dues_pending()));
            holder.tv_adapter_bi_policy_deposit.setText(String.valueOf(allElementDetails.get(position).getPolicy_deposit()));
            holder.tv_adapter_net_revival_amount.setText(String.valueOf(allElementDetails.get(position).getNet_revival_amount()));
            int colorPos = position % colors.length;
            convertView.setBackgroundColor(colors[colorPos]);

            return convertView;
        }

    }


    class Adapter_BI_BreakupDues extends BaseAdapter {

        class ViewHolder {
            TextView tv_adapter_bank_charges;
            TextView tv_adapter_bank_dated_interest;
            TextView tv_adapter_earlier_shortfall;
            TextView tv_adapter_duplicate_policy;

        }

        final List<HashMap<String, String>> allElementDetails;
        private final int[] colors = new int[]{Color.parseColor("#DCDBDB"),
                Color.parseColor("#E8E8E8")};

        Adapter_BI_BreakupDues(List<HashMap<String, String>> results) {
            allElementDetails = results;
        }

        public int getCount() {
            return allElementDetails.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater mInflater = LayoutInflater.from(context);
                convertView = mInflater.inflate(R.layout.adapter_breakup_dues,
                        null);
                holder = new ViewHolder();

                holder.tv_adapter_bank_charges = convertView.findViewById(R.id.tv_adapter_bank_charges);
                holder.tv_adapter_bank_dated_interest = convertView.findViewById(R.id.tv_adapter_bank_dated_interest);
                holder.tv_adapter_earlier_shortfall = convertView.findViewById(R.id.tv_adapter_earlier_shortfall);
                holder.tv_adapter_duplicate_policy = convertView.findViewById(R.id.tv_adapter_duplicate_policy);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tv_adapter_bank_charges.setText(String.valueOf(allElementDetails.get(position).get("bank_charges")));
            holder.tv_adapter_bank_dated_interest.setText(String.valueOf(allElementDetails.get(position).get("backdated_interest")));
            holder.tv_adapter_earlier_shortfall.setText(String.valueOf(allElementDetails.get(position).get("earlier_shortfall")));
            holder.tv_adapter_duplicate_policy.setText(String.valueOf(allElementDetails.get(position).get("duplicate_policy")));

            int colorPos = position % colors.length;
            convertView.setBackgroundColor(colors[colorPos]);

            return convertView;
        }
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

        if (downloadRevialQuoTationAsync != null) {
            downloadRevialQuoTationAsync.cancel(true);
        }
        if (service != null) {
            service.cancel(true);
        }
    }
}
