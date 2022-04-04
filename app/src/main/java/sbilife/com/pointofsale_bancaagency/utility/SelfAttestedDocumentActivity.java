package sbilife.com.pointofsale_bancaagency.utility;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.Utility;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;

public class SelfAttestedDocumentActivity extends AppCompatActivity {
    public static final int SIGNATURE_ACTIVITY = 1;
    private static final int DIALOG_ALERT = 10;
    public static Bitmap AgeBitmap;
    public static Bitmap AddressBitmap;
    public static Bitmap IncomeBitmap;
    public static Bitmap CommAddressBitmap;
    public static String status;
    public static List<String> lst_OTPSelfDeclarationBitmap = new ArrayList<String>();
    public static List<String> lst_AgeBitmap = new ArrayList<String>();
    public static List<String> lst_IdentityBitmap = new ArrayList<String>();
    public static List<String> lst_AddressBitmap = new ArrayList<String>();
    public static List<String> lst_IncomeBitmap = new ArrayList<String>();
    public static List<String> lst_AddendumAgeBitmap = new ArrayList<String>();
    public static List<String> lst_AddendumIdentityBitmap = new ArrayList<String>();
    public static List<String> lst_AddendumIncomeBitmap = new ArrayList<String>();
    public static List<String> lst_AddendumPanCardBitmap = new ArrayList<String>();
    public static List<String> lst_KeymanAddressBitmap = new ArrayList<String>();
    public static List<String> lst_KeymanIdentityBitmap = new ArrayList<String>();
    public static List<String> lst_KeymanIncomeBitmap = new ArrayList<String>();
    public static List<String> lst_OtherBitmap = new ArrayList<String>();
    public static List<String> lst_BankBitmap = new ArrayList<String>();
    public static List<String> lst_EFTBitmap = new ArrayList<String>();
    public static List<String> lst_CustomerPhotoBitmap = new ArrayList<String>();
    public static List<String> lst_CommAddressBitmap = new ArrayList<String>();
    public static File f;
    public static Bitmap scaled;
    public static File CommAddressFile;
    public static File AddendumAgeFile;
    public static File AddendumIdentityFile;
    public static File AddendumIncomeFile;
    public static File AddendumPanCardFile;
    public static File KeymanAddressFile;
    public static File KeymanIdentityFile;
    public static File KeymanIncomeFile;
    public static File file;
    private static Bitmap mBitmap;
    private static Bitmap previewBitmap;
    private static File mypath;
    private static String url;
    private static File AgeFile;
    private static File IdentityFile;
    private static File AddressFile;
    private static File IncomeFile;
    private static File OtherFile;
    private static File BankFile;
    private static File EFTFile;
    private static String Flag = "";
    public int count = 1;
    View mView;
    ImageView Imageview;
    ImageView iv_self_attestedImage_display;
    ImageView iv_self_ativ_self_attested_documenttested_document;

    Dialog d;
    private String current = null;
    // ImageView
    private ImageView iv_self_attested_document;
    // ImageButton
    private ImageButton ib_self_attestesd_signature;
    // RelativeLayout
    private RelativeLayout rl_self_attested_document_image;
    private String proposerSign = "";
    private String planName = "";
    private String ProposalNumber = "";
    private Utility objUtility;
    private StorageUtils mStorageUtils;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.new_self_attested);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        objUtility = new Utility();

        mStorageUtils = new StorageUtils();
        new CommonMethods().setApplicationToolbarMenu(this, "Documents");
        initialiseVariable();
        Intent intent = getIntent();
        Flag = intent.getStringExtra("FLAG");
        proposerSign = intent.getStringExtra("ProposerSign");
        planName = intent.getStringExtra("PlanName");

        ProposalNumber = AdditionalUtilityActivity.ProposalNumber;

        if (Flag.equalsIgnoreCase("FALSE")) {

            if ((AdditionalUtilityActivity.Check).equals("AgeProof")) {
                iv_self_attested_document
                        .setImageBitmap(AdditionalUtilityActivity.ReducedAgeBitmap);
            }
            if ((AdditionalUtilityActivity.Check).equals("IdentityProof")) {
                iv_self_attested_document
                        .setImageBitmap(AdditionalUtilityActivity.ReducedIdentityBitmap);
            }
            if ((AdditionalUtilityActivity.Check).equals("AddressProof")) {
                iv_self_attested_document
                        .setImageBitmap(AdditionalUtilityActivity.ReducedAddressBitmap);
            }

            if ((AdditionalUtilityActivity.Check).equals("IncomeProof")) {
                iv_self_attested_document
                        .setImageBitmap(AdditionalUtilityActivity.ReducedIncomeBitmap);
            }

            if ((AdditionalUtilityActivity.Check).equals("OtherProof")) {
                iv_self_attested_document
                        .setImageBitmap(AdditionalUtilityActivity.ReducedOtherBitmap);
            }

            if ((AdditionalUtilityActivity.Check).equals("BankProof")) {
                iv_self_attested_document
                        .setImageBitmap(AdditionalUtilityActivity.ReducedBankBitmap);
            }

            if ((AdditionalUtilityActivity.Check).equals("EFTProof")) {
                iv_self_attested_document
                        .setImageBitmap(AdditionalUtilityActivity.ReducedEFTBitmap);
            }

        }

        // byte[] signByteArray = Base64.decode(proposerSign, 0);
        // Bitmap bitmap = BitmapFactory.decodeByteArray(signByteArray, 0,
        // signByteArray.length);
        // ib_self_attestesd_signature.setImageBitmap(bitmap);
        // changeLayoutToImage();
    }

    private void initialiseVariable() {
        // ImageView
        iv_self_attested_document = findViewById(R.id.iv_self_attested_document);
        // iv_self_attestedImage_display = (ImageView)
        // findViewById(R.id.iv_self_attestedImage_display);
        // ImageButton
        ib_self_attestesd_signature = findViewById(R.id.ib_self_attestesd_signature);

        // Relativelayout
        rl_self_attested_document_image = findViewById(R.id.rl_self_attested_document_image);
    }

    // @Override
    // public void onBackPressed() {
    // changeLayoutToImage();
    // SelfAttestedDocumentActivity.this.finish();
    // }

    public void onBackPressed() {
        // TODO Auto-generated method stub
        changeLayoutToImage();
        super.onBackPressed();
    }

    public void onBack(View v) {
        // TODO Auto-generated method stub
        changeLayoutToImage();
        finish();
    }


    @SuppressWarnings("deprecation")
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_ALERT:
                Builder builder = new Builder(this);
                builder.setMessage("Do you want to contiune for Next Page");
                builder.setCancelable(true);
                builder.setPositiveButton("Yes", new OkOnClickListener());
                builder.setNegativeButton("No", new CancelOnClickListener());
                AlertDialog dialog = builder.create();
                dialog.show();
        }
        return super.onCreateDialog(id);
    }

    private void changeLayoutToImage() {

        //final float STROKE_WIDTH = 5f;
        //final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        //Paint paint = new Paint();
        //Path path = new Path();

        //float lastTouchX;
        //float lastTouchY;
        //final RectF dirtyRect = new RectF();

        // tempDir = Environment.getExternalStorageDirectory() + "/"
        // + getResources().getString(R.string.external_dir) + "/";

        //ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // uniqueId = "Proposalno_x01";
        // current = uniqueId + ".png";
        // File mypath = new File(folder, current);
        // File directory = cw.getDir(
        // getResources().getString(R.string.external_dir),
        // Context.MODE_PRIVATE);

        // prepareDirectory();
        // uniqueId = getTodaysDate() + "_" + getCurrentTime() + "_"
        // + Math.random();

        // mypath = new File(direct, current);
        rl_self_attested_document_image.setDrawingCacheEnabled(false);
        rl_self_attested_document_image.buildDrawingCache(false);
        Log.v("log_tag", "Width: " + rl_self_attested_document_image.getWidth());
        Log.v("log_tag",
                "Height: " + rl_self_attested_document_image.getHeight());
        if (mBitmap == null) {
            previewBitmap = Bitmap.createBitmap(
                    rl_self_attested_document_image.getWidth(),
                    rl_self_attested_document_image.getHeight(),
                    Bitmap.Config.RGB_565);

            // previewBitmap = Bitmap.createBitmap(2048, 600,
            // Bitmap.Config.ARGB_8888);

        }
        Canvas canvas = new Canvas(previewBitmap);
        // canvas.drawColor(Color.TRANSPARENT);
        try {

            // File myDir = new File("/sdcard/AttestedImages");
            // myDir.mkdirs();
            // Random generator = new Random(+);
            // int n = 10000;
            // n = generator.nextInt(n);
            // String fname = "Image-" + n + ".jpg";

            if (Flag.equalsIgnoreCase("FALSE")) {

                String uniqueId;
                if ((AdditionalUtilityActivity.Check).equals("AgeProof")) {

                    uniqueId = ProposalNumber + "_" + "X" + "1";
                    current = uniqueId + ".png";
                    AgeFile = mStorageUtils.createFileToAppSpecificDir(this, current);
                    if (AgeFile.exists())
                        AgeFile.delete();
                    try {
                        FileOutputStream mFileOutStream = new FileOutputStream(
                                AgeFile);
                        rl_self_attested_document_image.draw(canvas);
                        previewBitmap.compress(Bitmap.CompressFormat.PNG, 90,
                                mFileOutStream);
                        iv_self_attested_document.setImageBitmap(previewBitmap);
                        mFileOutStream.flush();
                        mFileOutStream.close();

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        // previewBitmap.compress(Bitmap.CompressFormat.PNG,
                        // 100,
                        // baos);

                        AdditionalUtilityActivity.ReducedAgeBitmap.compress(
                                Bitmap.CompressFormat.PNG, 70, baos);

                        byte[] arr = baos.toByteArray();
                        String result = Base64.encodeToString(arr,
                                Base64.DEFAULT);
                        lst_AgeBitmap.add(result);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if ((AdditionalUtilityActivity.Check).equals("IdentityProof")) {

                    uniqueId = ProposalNumber + "_" + "X" + "2";
                    current = uniqueId + ".png";
                    IdentityFile = mStorageUtils.createFileToAppSpecificDir(this, current);
                    if (IdentityFile.exists())
                        IdentityFile.delete();
                    try {
                        FileOutputStream mFileOutStream = new FileOutputStream(
                                IdentityFile);
                        rl_self_attested_document_image.draw(canvas);
                        previewBitmap.compress(Bitmap.CompressFormat.PNG, 90,
                                mFileOutStream);
                        iv_self_attested_document.setImageBitmap(previewBitmap);
                        mFileOutStream.flush();
                        mFileOutStream.close();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        // previewBitmap.compress(Bitmap.CompressFormat.PNG,
                        // 100,
                        // baos);

                        AdditionalUtilityActivity.ReducedIdentityBitmap
                                .compress(Bitmap.CompressFormat.PNG, 70, baos);

                        byte[] arr = baos.toByteArray();
                        String result = Base64.encodeToString(arr,
                                Base64.DEFAULT);
                        lst_IdentityBitmap.add(result);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if ((AdditionalUtilityActivity.Check).equals("AddressProof")) {

                    uniqueId = ProposalNumber + "_" + "X" + "3";
                    current = uniqueId + ".png";
                    AddressFile = mStorageUtils.createFileToAppSpecificDir(this, current);
                    // AddressFile = new File(myDir, fname);
                    if (AddressFile.exists())
                        AddressFile.delete();
                    try {
                        FileOutputStream mFileOutStream = new FileOutputStream(
                                AddressFile);
                        rl_self_attested_document_image.draw(canvas);
                        previewBitmap.compress(Bitmap.CompressFormat.PNG, 90,
                                mFileOutStream);
                        iv_self_attested_document.setImageBitmap(previewBitmap);
                        mFileOutStream.flush();
                        mFileOutStream.close();

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        // previewBitmap.compress(Bitmap.CompressFormat.PNG,
                        // 100,
                        // baos);

                        AdditionalUtilityActivity.ReducedAddressBitmap
                                .compress(Bitmap.CompressFormat.PNG, 70, baos);

                        byte[] arr = baos.toByteArray();
                        String result = Base64.encodeToString(arr,
                                Base64.DEFAULT);
                        lst_AddressBitmap.add(result);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if ((AdditionalUtilityActivity.Check).equals("IncomeProof")) {

                    uniqueId = ProposalNumber + "_" + "X" + "4";
                    current = uniqueId + ".png";
                    IncomeFile = mStorageUtils.createFileToAppSpecificDir(this, current);
                    // IncomeFile = new File(myDir, fname);
                    if (IncomeFile.exists())
                        IncomeFile.delete();
                    try {
                        FileOutputStream mFileOutStream = new FileOutputStream(
                                IncomeFile);
                        rl_self_attested_document_image.draw(canvas);
                        previewBitmap.compress(Bitmap.CompressFormat.PNG, 90,
                                mFileOutStream);

                        iv_self_attested_document.setImageBitmap(previewBitmap);
                        mFileOutStream.flush();
                        mFileOutStream.close();

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        // previewBitmap.compress(Bitmap.CompressFormat.PNG,
                        // 100,
                        // baos);
                        AdditionalUtilityActivity.ReducedIncomeBitmap.compress(
                                Bitmap.CompressFormat.PNG, 70, baos);

                        byte[] arr = baos.toByteArray();
                        String result = Base64.encodeToString(arr,
                                Base64.DEFAULT);
                        lst_IncomeBitmap.add(result);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if ((AdditionalUtilityActivity.Check).equals("BankProof")) {

                    uniqueId = ProposalNumber + "_" + "X" + "6";
                    current = uniqueId + ".png";
                    BankFile = mStorageUtils.createFileToAppSpecificDir(this, current);
                    // IncomeFile = new File(myDir, fname);
                    if (BankFile.exists())
                        BankFile.delete();
                    try {
                        FileOutputStream mFileOutStream = new FileOutputStream(
                                BankFile);
                        rl_self_attested_document_image.draw(canvas);
                        previewBitmap.compress(Bitmap.CompressFormat.PNG, 90,
                                mFileOutStream);
                        iv_self_attested_document.setImageBitmap(previewBitmap);
                        mFileOutStream.flush();
                        mFileOutStream.close();

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        // previewBitmap.compress(Bitmap.CompressFormat.PNG,
                        // 100,
                        // baos);

                        AdditionalUtilityActivity.ReducedBankBitmap.compress(
                                Bitmap.CompressFormat.PNG, 70, baos);

                        byte[] arr = baos.toByteArray();
                        String result = Base64.encodeToString(arr,
                                Base64.DEFAULT);
                        lst_BankBitmap.add(result);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if ((AdditionalUtilityActivity.Check).equals("EFTProof")) {

                    uniqueId = ProposalNumber + "_" + "X" + "7";
                    current = uniqueId + ".png";
                    EFTFile = mStorageUtils.createFileToAppSpecificDir(this, current);
                    // IncomeFile = new File(myDir, fname);
                    if (EFTFile.exists())
                        EFTFile.delete();
                    try {
                        FileOutputStream mFileOutStream = new FileOutputStream(
                                EFTFile);
                        rl_self_attested_document_image.draw(canvas);
                        previewBitmap.compress(Bitmap.CompressFormat.PNG, 90,
                                mFileOutStream);
                        iv_self_attested_document.setImageBitmap(previewBitmap);
                        mFileOutStream.flush();
                        mFileOutStream.close();

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        // previewBitmap.compress(Bitmap.CompressFormat.PNG,
                        // 100,
                        // baos);

                        AdditionalUtilityActivity.ReducedEFTBitmap.compress(
                                Bitmap.CompressFormat.PNG, 70, baos);

                        byte[] arr = baos.toByteArray();
                        String result = Base64.encodeToString(arr,
                                Base64.DEFAULT);
                        lst_EFTBitmap.add(result);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if ((AdditionalUtilityActivity.Check).equals("OtherProof")) {

                    uniqueId = ProposalNumber + "_" + "X" + "5";
                    current = uniqueId + ".png";
                    OtherFile = mStorageUtils.createFileToAppSpecificDir(this, current);
                    // IncomeFile = new File(myDir, fname);
                    if (OtherFile.exists())
                        OtherFile.delete();
                    try {
                        FileOutputStream mFileOutStream = new FileOutputStream(
                                OtherFile);
                        rl_self_attested_document_image.draw(canvas);
                        previewBitmap.compress(Bitmap.CompressFormat.PNG, 90,
                                mFileOutStream);
                        iv_self_attested_document.setImageBitmap(previewBitmap);
                        mFileOutStream.flush();
                        mFileOutStream.close();

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        // previewBitmap.compress(Bitmap.CompressFormat.PNG,
                        // 100,
                        // baos);

                        AdditionalUtilityActivity.ReducedOtherBitmap.compress(
                                Bitmap.CompressFormat.PNG, 70, baos);

                        byte[] arr = baos.toByteArray();
                        String result = Base64.encodeToString(arr,
                                Base64.DEFAULT);
                        lst_OtherBitmap.add(result);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                Log.v("log_tag", "url: " + url);

                // In case you want to delete the file
                // boolean deleted = mypath.delete();
                // Log.v("log_tag","deleted: " + mypath.toString() + deleted);
                // If you want to convert the image to string use base64
                // converter

            }

        } catch (Exception e) {
            Log.v("log_tag", e.toString());
        }

    }

    public boolean createAOBDOcumentPdf(File mFile, File sourceFile, String strName) {

        boolean running = true;

        try {

            Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                    Font.BOLD, BaseColor.WHITE);

            Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 10,
                    Font.NORMAL);

            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(document, new FileOutputStream(mFile.getAbsolutePath()));

            document.open();

            Paragraph DocumentUpload_Para_Header = new Paragraph();
            DocumentUpload_Para_Header.add(new Paragraph(strName, headerBold));

            PdfPTable DocumentUpload_headertable = new PdfPTable(1);
            DocumentUpload_headertable.setWidthPercentage(100);
            PdfPCell DocumentUpload_c1 = new PdfPCell(new Phrase(
                    DocumentUpload_Para_Header));
            DocumentUpload_c1.setBackgroundColor(BaseColor.DARK_GRAY);
            DocumentUpload_c1.setPadding(5);
            DocumentUpload_c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            DocumentUpload_headertable.addCell(DocumentUpload_c1);
            DocumentUpload_headertable
                    .setHorizontalAlignment(Element.ALIGN_LEFT);

            document.add(DocumentUpload_headertable);

            PdfPTable DD_table = new PdfPTable(1);
            DD_table.setWidthPercentage(100);

            Image img_doc = Image.getInstance(sourceFile.getAbsolutePath());
            /*img_doc.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
            img_doc.setAbsolutePosition(
                    0, (PageSize.A4.getHeight() - DD_table.getTotalHeight()) / 2);*/

            PdfPCell DocumentUpload_row2_cell = new PdfPCell();
            DocumentUpload_row2_cell
                    .setHorizontalAlignment(Element.ALIGN_CENTER);
            DocumentUpload_row2_cell.setPadding(5);
            DocumentUpload_row2_cell.setImage(img_doc);

            DD_table.addCell(DocumentUpload_row2_cell);

            document.add(DD_table);

            document.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            running = false;
        }

        return running;
    }

    @SuppressWarnings("unused")
    public void createDocumentPdf(int Increment, String proofOf,
                                  String documentName, List<String> lst_Bitmap, String proposerSign) {

        try {

            Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                    Font.BOLD, BaseColor.WHITE);

			/*Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 10,
					Font.BOLD);*/
            Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 10,
                    Font.NORMAL);
            //float[] columnWidths2 = { 1f, 2f };

            mypath = mStorageUtils.createFileToAppSpecificDir(this, AdditionalUtilityActivity.ProposalNumber + "_" + "X" + Increment
                    + "." + "pdf");
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter pdf_writer = null;
            pdf_writer = PdfWriter.getInstance(document, new FileOutputStream(
                    mypath.getAbsolutePath()));

            document.open();

            Paragraph DocumentUpload_Para_Header = new Paragraph();
            DocumentUpload_Para_Header.add(new Paragraph(proofOf + ""
                    + documentName, headerBold));

            PdfPTable DocumentUpload_headertable = new PdfPTable(1);
            DocumentUpload_headertable.setWidthPercentage(100);
            PdfPCell DocumentUpload_c1 = new PdfPCell(new Phrase(
                    DocumentUpload_Para_Header));
            DocumentUpload_c1.setBackgroundColor(BaseColor.DARK_GRAY);
            DocumentUpload_c1.setPadding(5);
            DocumentUpload_c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            DocumentUpload_headertable.addCell(DocumentUpload_c1);
            DocumentUpload_headertable
                    .setHorizontalAlignment(Element.ALIGN_LEFT);

            document.add(DocumentUpload_headertable);

            for (int i = 0; i < lst_Bitmap.size(); i++) {
                if (!(i == 0) && i % 2 == 0) {
                    document.newPage();
                }
                PdfPTable DD_table = new PdfPTable(1);
                DD_table.setWidthPercentage(100);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                byte[] encodeByte = Base64.decode(lst_Bitmap.get(i),
                        Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                        encodeByte.length);
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                Image img_Age = Image.getInstance(stream.toByteArray());

                PdfPCell DocumentUpload_row2_cell = new PdfPCell();
                DocumentUpload_row2_cell
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                DocumentUpload_row2_cell.setPadding(5);
                DocumentUpload_row2_cell.setImage(img_Age);

                DD_table.addCell(DocumentUpload_row2_cell);

                document.add(DD_table);
            }

//			PdfPTable BD_signatureofProposer = new PdfPTable(2);
//			BD_signatureofProposer.setWidthPercentage(100);
//			BD_signatureofProposer.setWidths(columnWidths2);
//			PdfPCell BD_signatureofProposer_cell1 = new PdfPCell(new Paragraph(
//					"Signature of Proposer", small_normal));
//			PdfPCell BD_signatureofProposer_cell2 = new PdfPCell();
//			BD_signatureofProposer_cell2.setFixedHeight(60f);
//			if ((!proposerSign.equals("") && (proposerSign != null))) {
//				byte[] fbyt_Cr_BD_signatureofProposer_sign = Base64.decode(
//						proposerSign, 0);
//				Bitmap Cr_BD_signatureofProposerbitmap = BitmapFactory
//						.decodeByteArray(fbyt_Cr_BD_signatureofProposer_sign,
//								0, fbyt_Cr_BD_signatureofProposer_sign.length);
//
//				ByteArrayOutputStream BD_signatureofProposer_sign_stream = new ByteArrayOutputStream();
//
//				(Cr_BD_signatureofProposerbitmap).compress(
//						Bitmap.CompressFormat.PNG, 50,
//						BD_signatureofProposer_sign_stream);
//				Image signatureofProposer_sign_signature = Image
////						.getInstance(BD_signatureofProposer_sign_stream
////								.toByteArray());
//				BD_signatureofProposer_cell2
//						.setImage(signatureofProposer_sign_signature);
//			}
//			BD_signatureofProposer_cell1.setPadding(5);
//			BD_signatureofProposer_cell2.setPadding(5);
//
//			BD_signatureofProposer_cell2
//					.setHorizontalAlignment(Element.ALIGN_CENTER);
//
//			BD_signatureofProposer.addCell(BD_signatureofProposer_cell1);
//			BD_signatureofProposer.addCell(BD_signatureofProposer_cell2);
//
//			document.add(BD_signatureofProposer);

            // lst_AddressBitmap.clear();
            // lst_AgeBitmap.clear();
            // lst_IdentityBitmap.clear();
            // lst_IncomeBitmap.clear();
            // pdf_writer.setFullCompression();

            PdfPTable DD_table7 = new PdfPTable(1);
            DD_table7.setWidthPercentage(100);
            PdfPCell DocumentUpload_row7_cell_1 = new PdfPCell(new Paragraph(
                    "Verified with original.", small_normal));
            DocumentUpload_row7_cell_1.setPadding(5);
            DocumentUpload_row7_cell_1.setHorizontalAlignment(Element.ALIGN_LEFT);
            DD_table7.addCell(DocumentUpload_row7_cell_1);

            document.add(DD_table7);


            document.close();

        } catch (Exception e) {
        }
    }

    public void createDocumentPdf_groups(int Increment, String proofOf,
                                         String documentName, List<String> lst_Bitmap, String QuoteORProposalNo) {

        try {

            Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                    Font.BOLD, BaseColor.WHITE);

			/*Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 10,
                    Font.BOLD);*/
            Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 10,
                    Font.NORMAL);
            //float[] columnWidths2 = { 1f, 2f };

            if (proofOf.equalsIgnoreCase("One Pager Proof ")) {
                mypath = mStorageUtils.createFileToAppSpecificDir(this, QuoteORProposalNo + "_" + "S" + Increment
                        + "." + "pdf");
            } else {
                mypath = mStorageUtils.createFileToAppSpecificDir(this, QuoteORProposalNo + "_" + "X" + Increment
                        + "." + "pdf");
            }

            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter pdf_writer = null;
            pdf_writer = PdfWriter.getInstance(document, new FileOutputStream(
                    mypath.getAbsolutePath()));

            document.open();

            Paragraph DocumentUpload_Para_Header = new Paragraph();
            DocumentUpload_Para_Header.add(new Paragraph(proofOf + ""
                    + documentName, headerBold));

            PdfPTable DocumentUpload_headertable = new PdfPTable(1);
            DocumentUpload_headertable.setWidthPercentage(100);
            PdfPCell DocumentUpload_c1 = new PdfPCell(new Phrase(
                    DocumentUpload_Para_Header));
            DocumentUpload_c1.setBackgroundColor(BaseColor.DARK_GRAY);
            DocumentUpload_c1.setPadding(5);
            DocumentUpload_c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            DocumentUpload_headertable.addCell(DocumentUpload_c1);
            DocumentUpload_headertable
                    .setHorizontalAlignment(Element.ALIGN_LEFT);

            document.add(DocumentUpload_headertable);

            for (int i = 0; i < lst_Bitmap.size(); i++) {
                if (!(i == 0) && i % 2 == 0) {
                    document.newPage();
                }
                PdfPTable DD_table = new PdfPTable(1);
                DD_table.setWidthPercentage(100);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                byte[] encodeByte = Base64.decode(lst_Bitmap.get(i),
                        Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                        encodeByte.length);
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                Image img_Age = Image.getInstance(stream.toByteArray());

                PdfPCell DocumentUpload_row2_cell = new PdfPCell();
                DocumentUpload_row2_cell
                        .setHorizontalAlignment(Element.ALIGN_CENTER);
                DocumentUpload_row2_cell.setPadding(5);
                DocumentUpload_row2_cell.setImage(img_Age);

                DD_table.addCell(DocumentUpload_row2_cell);

                document.add(DD_table);
            }

            PdfPTable DD_table7 = new PdfPTable(1);
            DD_table7.setWidthPercentage(100);
            PdfPCell DocumentUpload_row7_cell_1 = new PdfPCell(new Paragraph(
                    "Verified with original.", small_normal));
            DocumentUpload_row7_cell_1.setPadding(5);
            DocumentUpload_row7_cell_1.setHorizontalAlignment(Element.ALIGN_LEFT);
            DD_table7.addCell(DocumentUpload_row7_cell_1);

            document.add(DD_table7);


            document.close();

        } catch (Exception e) {
        }
    }

    private final class CancelOnClickListener implements
            DialogInterface.OnClickListener {

        public void onClick(DialogInterface dialog, int which) {

            SelfAttestedDocumentActivity.this.finish();
        }
    }

    private final class OkOnClickListener implements
            DialogInterface.OnClickListener {

        public void onClick(DialogInterface dialog, int which) {
            Toast.makeText(getApplicationContext(),
                    "Cancle selected, activity continues", Toast.LENGTH_LONG)
                    .show();

        }
    }
}
