package sbilife.com.pointofsale_bancaagency.ckyc;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.text.Html;
import android.util.Base64;

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
import com.itextpdf.text.pdf.draw.LineSeparator;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.agent_on_boarding.ActivityAOBPersonalInfo;
import sbilife.com.pointofsale_bancaagency.agent_on_boarding.Activity_AOB_Authentication;
import sbilife.com.pointofsale_bancaagency.common.AsyncUploadFile_Common;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.posp_ra.Activity_POSP_RA_Authentication;
import sbilife.com.pointofsale_bancaagency.posp_ra.Activity_POSP_RA_PersonalInfo;

public class AsyncGetcKYCSearchDetail extends AsyncTask<String, Void, String> {

    private final String Namespace = "http://tempuri.org/";
    private final String METHOD_NAME_GETCKYCDETAIL = "cKYC";
    private final String METHOD_NAME_UPLOAD_ALL_AOB_DOC = "UploadFile_AgentEnroll";
    private final Context mContext;
    private final CommonMethods mCommonMethods;
    private final String KYC_DATE = "";
    private final StorageUtils mStorageUtils;
    private final InterfaceCKYCProcessCompletion mInterfaceCKYCProcessCompletion;
    private volatile boolean running = true;
    private ProgressDialog mProgressDialog;
    private String str_res_cKYC_dtls = "";
    private String CKYC_NO = "";
    private String str_user_dob = "";
    private String FULLNAME = "";
    private String str_res_cKYC_download_dtls = "";
    private String MAIDEN_FULLNAME = "";
    private String FATHERSPOUSE_FLAG = "";
    private String FATHER_FULLNAME = "";
    private String MOTHER_FULLNAME = "";
    private String GENDER = "";
    private String DOB = "";
    private String PAN = "";
    private String FORM_SIXTY = "";
    private String PERM_LINE1 = "";
    private String PERM_LINE2 = "";
    private String PERM_LINE3 = "";
    private String PERM_CITY = "";
    private String PERM_DIST = "";
    private String PERM_STATE = "";
    private String PERM_COUNTRY = "";
    private String PERM_PIN = "";
    private String PERM_POA = "";
    private String PERM_CORRES_SAMEFLAG = "";
    private String CORRES_LINE1 = "";
    private String CORRES_LINE2 = "";
    private String CORRES_LINE3 = "";
    private String CORRES_CITY = "";
    private String CORRES_DIST = "";
    private String CORRES_STATE = "";
    private String CORRES_COUNTRY = "";
    private String CORRES_PIN = "";
    private String CORRES_POA = "";
    private String RESI_STD_CODE = "";
    private String RESI_TEL_NUM = "";
    private String OFF_STD_CODE = "";
    private String OFF_TEL_NUM = "";
    private String MOB_NUM = "";
    private String EMAIL = "";
    private String DEC_DATE = "";
    private String DEC_PLACE = "";
    private String IDENT_TYPE1 = "";
    private String IDENT_NUM1 = "";
    private String IDENT_TYPE2 = "";
    private String IDENT_NUM2 = "";
    private String IDENT_TYPE3 = "";
    private String IDENT_NUM3 = "";
    private String IDENT_TYPE4 = "";
    private String IDENT_NUM4 = "";
    private String IDENT_TYPE5 = "";
    private String IDENT_NUM5 = "";
    private String IDENT_TYPE6 = "";
    private String IDENT_NUM6 = "";
    private String IMAGE_TYPE1 = "";
    private String IMAGE_CODE1 = "";
    private String IMAGE_DATA1 = "";
    private String IMAGE_TYPE2 = "";
    private String IMAGE_CODE2 = "";
    private String IMAGE_DATA2 = "";
    private String IMAGE_TYPE3 = "";
    private String IMAGE_CODE3 = "";
    private String IMAGE_DATA3 = "";
    private String IMAGE_TYPE4 = "";
    private String IMAGE_CODE4 = "";
    private String IMAGE_DATA4 = "";
    private String IMAGE_TYPE5 = "";
    private String IMAGE_CODE5 = "";
    private String IMAGE_DATA5 = "";
    private String IMAGE_TYPE6 = "";
    private String IMAGE_CODE6 = "";
    private String IMAGE_DATA6 = "";
    private String str_CKYC_PAN_no = "";
    private String str_QR_code_Photo = "";
    private String str_Doc_Abbreviation = "";
    private String strFormatedCKYCDetails = "";
    private String strEnrollType = "";
    private int str_CKYC_increment = 0;

    public AsyncGetcKYCSearchDetail(Context mContext, String strPAN, String strDOB, String strEnrollType,
                                    InterfaceCKYCProcessCompletion mInterfaceCKYCProcessCompletion) {
        this.mContext = mContext;
        this.str_CKYC_PAN_no = strPAN;
        this.str_user_dob = strDOB;
        this.strEnrollType = strEnrollType;
        this.mInterfaceCKYCProcessCompletion = mInterfaceCKYCProcessCompletion;

        mCommonMethods = new CommonMethods();
        mStorageUtils = new StorageUtils();
        mProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setTitle(Html
                .fromHtml("<font color='#00a1e3'><b>Please Wait...<b></font>"));
    }

    public int getStr_CKYC_increment() {
        return str_CKYC_increment;
    }

    public void setStr_CKYC_increment(int str_CKYC_increment) {
        this.str_CKYC_increment = str_CKYC_increment;
    }

    @Override
    protected void onPreExecute() {
        if (mProgressDialog != null)
            mProgressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        if (mCommonMethods.isNetworkConnected(mContext)) {

            try {

                SoapObject request = new SoapObject(Namespace,
                        METHOD_NAME_GETCKYCDETAIL);
                String ID_TYPE = "C";

                JSONObject json_obj = new JSONObject();
                json_obj.put("MODE", "SEARCH");
                json_obj.put("ID_TYPE", ID_TYPE);
                json_obj.put("ID_NO", str_CKYC_PAN_no /*NBM_PROP_PANCARD_NO*/);
                json_obj.put("Source", "SBIL");
                json_obj.put("AuthKey", "$Sbilife@12345$");
                request.addProperty("strInput", json_obj.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                //allowAllSSL();
                mCommonMethods.TLSv12Enable();
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                HttpTransportSE androidHttpTransportSE = new HttpTransportSE(ServiceURL.SERVICE_URL,
                        100000);
                androidHttpTransportSE.call(Namespace + METHOD_NAME_GETCKYCDETAIL,
                        envelope);
                Object response = envelope.getResponse();
                SoapPrimitive sa = null;
                sa = (SoapPrimitive) envelope.getResponse();

                str_res_cKYC_dtls = sa.toString();
                if (!str_res_cKYC_dtls.contains("PID_DATA")) {
                    str_res_cKYC_dtls = "0";
                }

                return str_res_cKYC_dtls;
            } catch (Exception e) {
                running = false;
                str_res_cKYC_dtls = "0";
                return e.getMessage();
            }

        } else {
            running = false;
            return mCommonMethods.WEEK_INTERNET_MESSAGE;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();

        if (running) {
            if (!str_res_cKYC_dtls.equals("0")) {
                try {
                    JSONObject json_Search = new JSONObject(str_res_cKYC_dtls);
                    String Output = json_Search.get("Output").toString();
                    String Error = json_Search.get("Error").toString();
                    if (Error.equals("")) {
                        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                        org.w3c.dom.Document doc = dBuilder.parse(new InputSource(new StringReader(Output)));
                        doc.getDocumentElement().normalize();
                        NodeList nList = doc.getElementsByTagName("PID_DATA");
                        for (int temp = 0; temp < nList.getLength(); temp++) {
                            Node nNode = nList.item(temp);
                            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                org.w3c.dom.Element eElement = (org.w3c.dom.Element) nNode;
                                CKYC_NO = eElement.getElementsByTagName("CKYC_NO").item(0).getTextContent();
                                if (CKYC_NO == null) {
                                    CKYC_NO = "";
                                }
                            }
                        }
                        str_res_cKYC_download_dtls = "";
                        new AsyncGetcKYCDownloadDetail().execute();
                    } else {
                        //mCommonMethods.showToast(mContext, Error);
                        mInterfaceCKYCProcessCompletion.onCKYCProcessComppletion(InterfaceCKYCProcessCompletion.CKYC_SEARCH_PROCESS,
                                false, Error);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    //mCommonMethods.showToast(mContext, "Error in KYC XML");
                    mInterfaceCKYCProcessCompletion.onCKYCProcessComppletion(InterfaceCKYCProcessCompletion.CKYC_SEARCH_PROCESS,
                            false, "Error in KYC XML");
                }

            } else {
                //mCommonMethods.showToast(mContext, "cKYC Data not Retrived");
                mInterfaceCKYCProcessCompletion.onCKYCProcessComppletion(InterfaceCKYCProcessCompletion.CKYC_SEARCH_PROCESS,
                        false, "cKYC Data not Retrived");
            }
        } else {
            //mCommonMethods.showToast(mContext, s);
            mInterfaceCKYCProcessCompletion.onCKYCProcessComppletion(InterfaceCKYCProcessCompletion.CKYC_SEARCH_PROCESS,
                    false, s);
        }
    }

    public void UploadCKYCDocuments() {

        try {


            String email_Id = mCommonMethods.GetUserEmail(mContext);
            String mobile_No = mCommonMethods.GetUserMobile(mContext);
            byte[] CKYCbytes = null;
            String str_base64_data = "", KYC_File_ext = "";

            if (str_CKYC_increment == 1) {
                str_base64_data = IMAGE_DATA1;
                KYC_File_ext = IMAGE_TYPE1;
                if (IMAGE_CODE1.equalsIgnoreCase("04")
                        || IMAGE_CODE1.equalsIgnoreCase("36")
                        || IMAGE_CODE1.equalsIgnoreCase("37")) {
                    str_Doc_Abbreviation = "AADHAR";
                } else if (IMAGE_CODE1.equalsIgnoreCase("05")) {
                    str_Doc_Abbreviation = "PASSPORT";
                } else if (IMAGE_CODE1.equalsIgnoreCase("06")) {
                    str_Doc_Abbreviation = "DL";
                } else if (IMAGE_CODE1.equalsIgnoreCase("07")) {
                    str_Doc_Abbreviation = "VID";
                } else {
                    str_Doc_Abbreviation = "OTHER";
                }
                CKYCbytes = Base64.decode(str_base64_data, Base64.DEFAULT);
            }
            if (str_CKYC_increment == 2) {
                str_base64_data = IMAGE_DATA2;
                KYC_File_ext = IMAGE_TYPE2;
                if (IMAGE_CODE2.equalsIgnoreCase("04")
                        || IMAGE_CODE1.equalsIgnoreCase("36")
                        || IMAGE_CODE1.equalsIgnoreCase("37")) {
                    str_Doc_Abbreviation = "AADHAR";
                } else if (IMAGE_CODE2.equalsIgnoreCase("05")) {
                    str_Doc_Abbreviation = "PASSPORT";
                } else if (IMAGE_CODE2.equalsIgnoreCase("06")) {
                    str_Doc_Abbreviation = "DL";
                } else if (IMAGE_CODE2.equalsIgnoreCase("07")) {
                    str_Doc_Abbreviation = "VID";
                } else {
                    str_Doc_Abbreviation = "OTHER";
                }
                CKYCbytes = Base64.decode(str_base64_data, Base64.DEFAULT);
            }
            if (str_CKYC_increment == 3) {
                str_base64_data = IMAGE_DATA3;
                KYC_File_ext = IMAGE_TYPE3;
                if (IMAGE_CODE3.equalsIgnoreCase("04")
                        || IMAGE_CODE1.equalsIgnoreCase("36")
                        || IMAGE_CODE1.equalsIgnoreCase("37")) {
                    str_Doc_Abbreviation = "AADHAR";
                } else if (IMAGE_CODE3.equalsIgnoreCase("05")) {
                    str_Doc_Abbreviation = "PASSPORT";
                } else if (IMAGE_CODE3.equalsIgnoreCase("06")) {
                    str_Doc_Abbreviation = "DL";
                } else if (IMAGE_CODE3.equalsIgnoreCase("07")) {
                    str_Doc_Abbreviation = "VID";
                } else {
                    str_Doc_Abbreviation = "OTHER";
                }
                CKYCbytes = Base64.decode(str_base64_data, Base64.DEFAULT);
            }
            if (str_CKYC_increment == 4) {
                str_base64_data = IMAGE_DATA4;
                KYC_File_ext = IMAGE_TYPE4;
                if (IMAGE_CODE4.equalsIgnoreCase("04")
                        || IMAGE_CODE1.equalsIgnoreCase("36")
                        || IMAGE_CODE1.equalsIgnoreCase("37")) {
                    str_Doc_Abbreviation = "AADHAR";
                } else if (IMAGE_CODE4.equalsIgnoreCase("05")) {
                    str_Doc_Abbreviation = "PASSPORT";
                } else if (IMAGE_CODE4.equalsIgnoreCase("06")) {
                    str_Doc_Abbreviation = "DL";
                } else if (IMAGE_CODE4.equalsIgnoreCase("07")) {
                    str_Doc_Abbreviation = "VID";
                } else {
                    str_Doc_Abbreviation = "OTHER";
                }
                CKYCbytes = Base64.decode(str_base64_data, Base64.DEFAULT);
            }
            if (str_CKYC_increment == 5) {
                str_base64_data = IMAGE_DATA5;
                KYC_File_ext = IMAGE_TYPE5;
                if (IMAGE_CODE5.equalsIgnoreCase("04")
                        || IMAGE_CODE1.equalsIgnoreCase("36")
                        || IMAGE_CODE1.equalsIgnoreCase("37")) {
                    str_Doc_Abbreviation = "AADHAR";
                } else if (IMAGE_CODE5.equalsIgnoreCase("05")) {
                    str_Doc_Abbreviation = "PASSPORT";
                } else if (IMAGE_CODE5.equalsIgnoreCase("06")) {
                    str_Doc_Abbreviation = "DL";
                } else if (IMAGE_CODE5.equalsIgnoreCase("07")) {
                    str_Doc_Abbreviation = "VID";
                } else {
                    str_Doc_Abbreviation = "OTHER";
                }
                CKYCbytes = Base64.decode(str_base64_data, Base64.DEFAULT);
            }
            if (str_CKYC_increment == 6) {
                str_base64_data = IMAGE_DATA6;
                KYC_File_ext = IMAGE_TYPE6;
                if (IMAGE_CODE6.equalsIgnoreCase("04")
                        || IMAGE_CODE1.equalsIgnoreCase("36")
                        || IMAGE_CODE1.equalsIgnoreCase("37")) {
                    str_Doc_Abbreviation = "AADHAR";
                } else if (IMAGE_CODE6.equalsIgnoreCase("05")) {
                    str_Doc_Abbreviation = "PASSPORT";
                } else if (IMAGE_CODE6.equalsIgnoreCase("06")) {
                    str_Doc_Abbreviation = "DL";
                } else if (IMAGE_CODE6.equalsIgnoreCase("07")) {
                    str_Doc_Abbreviation = "VID";
                } else {
                    str_Doc_Abbreviation = "OTHER";
                }
                CKYCbytes = Base64.decode(str_base64_data, Base64.DEFAULT);
            }
                /*if (str_CKYC_increment == 25) {

                    String strFileName = str_CKYC_PAN_no + "_" + "X" + 1 + "." + "pdf";
                    File f = mStorageUtils.createFileToAppSpecificDir(mContext, strFileName);

                    CKYCbytes = mCommonMethods.read(f);

                    KYC_File_ext = "pdf";
                }*/

            SoapObject request = new SoapObject(Namespace, METHOD_NAME_UPLOAD_ALL_AOB_DOC);

            request.addProperty("f", CKYCbytes);

                /*if (str_CKYC_increment.equalsIgnoreCase("25")) {

                    request.addProperty("fileName", str_CKYC_PAN_no + "_OTHER_25"
                            + "." + KYC_File_ext);
                } else {*/

            String strDoc = "";
            if (strEnrollType.equals("New")) {
                strDoc = str_Doc_Abbreviation.equals("OTHER") ? "others" : "communication_address_proof";
            } else if (strEnrollType.equals(mCommonMethods.str_posp_ra_customer_type)) {
                strDoc = str_Doc_Abbreviation.equals("OTHER") ? "others" : "communication_proof";
            }

            request.addProperty("fileName", str_CKYC_PAN_no + "_" + strDoc + "." + KYC_File_ext);

            //}

            request.addProperty("PAN", str_CKYC_PAN_no);

            mCommonMethods.appendSecurityParams(mContext, request, "", "");

            if (strDoc.equalsIgnoreCase("others")) {

                if (str_CKYC_increment < 6) {
                    str_CKYC_increment++;
                    UploadCKYCDocuments();
                } else {
                    get_personal_info_xml();
                }
            } else {
                new AsyncUploadFile_Common(mContext,
                        new AsyncUploadFile_Common.Interface_Upload_File_Common() {
                            @Override
                            public void onUploadComplete(Boolean result) {

                                if (result) {

                                    mCommonMethods.showToast(mContext, "CKYC " + str_Doc_Abbreviation + " document Uploaded Successfully!!!");

                                    if (str_CKYC_increment == 1) {
                                        if (!IMAGE_DATA2.equalsIgnoreCase("")) {
                                            str_CKYC_increment = 2;
                                            UploadCKYCDocuments();
                                        } else {
                                            str_CKYC_increment = 3;
                                            UploadCKYCDocuments();
                                        }

                                    } else if (str_CKYC_increment == 2) {
                                        if (!IMAGE_DATA3.equalsIgnoreCase("")) {
                                            str_CKYC_increment = 3;
                                            UploadCKYCDocuments();
                                        } else {
                                            str_CKYC_increment = 4;
                                            UploadCKYCDocuments();
                                        }

                                    } else if (str_CKYC_increment == 3) {
                                        if (!IMAGE_DATA4.equalsIgnoreCase("")) {
                                            str_CKYC_increment = 4;
                                            UploadCKYCDocuments();
                                        } else {
                                            str_CKYC_increment = 5;
                                            UploadCKYCDocuments();
                                        }
                                    } else if (str_CKYC_increment == 4) {
                                        if (!IMAGE_DATA5.equalsIgnoreCase("")) {
                                            str_CKYC_increment = 5;
                                            UploadCKYCDocuments();
                                        } else {
                                            str_CKYC_increment = 6;
                                            UploadCKYCDocuments();
                                        }
                                    } else if (str_CKYC_increment == 5) {
                                        if (!IMAGE_DATA6.equalsIgnoreCase("")) {
                                            str_CKYC_increment = 6;
                                            UploadCKYCDocuments();
                                        } else {
                                            get_personal_info_xml();
                                        }
                                    } else if (str_CKYC_increment == 6) {
                                        get_personal_info_xml();
                                    } else {
                                        get_personal_info_xml();
                                    }
                                } else {
                                    if (str_CKYC_increment < 6) {
                                        str_CKYC_increment++;
                                        UploadCKYCDocuments();
                                    } else {
                                        get_personal_info_xml();
                                    }
                                }
                            }
                        }, request, METHOD_NAME_UPLOAD_ALL_AOB_DOC).execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean GetCKYCDetails() {

        boolean isRunning = true;
        /* getKYCDetails */
        if (!str_res_cKYC_download_dtls.equalsIgnoreCase("")) {
            // String strKYCAppVersion = db.getKYCAppVeraion(QuatationNumber);
            try {
                JSONObject json_Search = new JSONObject(str_res_cKYC_download_dtls);

                String Output = json_Search.get("Output").toString();

                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                org.w3c.dom.Document doc = dBuilder.parse(new InputSource(new StringReader(Output)));
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("PID_DATA");
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        org.w3c.dom.Element eElement = (org.w3c.dom.Element) nNode;
                        CKYC_NO = eElement.getElementsByTagName("CKYC_NO").item(0).getTextContent();
                        if (CKYC_NO == null) {
                            CKYC_NO = "";
                        }

                        FULLNAME = eElement.getElementsByTagName("FULLNAME").item(0).getTextContent();
                        if (FULLNAME == null) {
                            FULLNAME = "";
                        }

                        MAIDEN_FULLNAME = eElement.getElementsByTagName("MAIDEN_FULLNAME").item(0).getTextContent();
                        if (MAIDEN_FULLNAME == null) {
                            MAIDEN_FULLNAME = "";
                        }

                        FATHERSPOUSE_FLAG = eElement.getElementsByTagName("FATHERSPOUSE_FLAG").item(0).getTextContent();
                        if (FATHERSPOUSE_FLAG == null) {
                            FATHERSPOUSE_FLAG = "";
                        }


                        FATHER_FULLNAME = eElement.getElementsByTagName("FATHER_FULLNAME").item(0).getTextContent();
                        if (FATHER_FULLNAME == null) {
                            FATHER_FULLNAME = "";
                        }


                        MOTHER_FULLNAME = eElement.getElementsByTagName("MOTHER_FULLNAME").item(0).getTextContent();
                        if (MOTHER_FULLNAME == null) {
                            MOTHER_FULLNAME = "";
                        }


                        GENDER = eElement.getElementsByTagName("GENDER").item(0).getTextContent();
                        if (GENDER == null) {
                            GENDER = "";
                        }
                        DOB = eElement.getElementsByTagName("DOB").item(0).getTextContent();
                        if (DOB == null) {
                            DOB = "";
                        }

                        PAN = eElement.getElementsByTagName("PAN").item(0).getTextContent();
                        if (PAN == null) {
                            PAN = "";
                        }
                        if (Output.contains("FORM_SIXTY")) {
                            FORM_SIXTY = eElement.getElementsByTagName("FORM_SIXTY").item(0).getTextContent();
                            if (FORM_SIXTY == null) {
                                FORM_SIXTY = "";
                            }
                        }

                        PERM_LINE1 = eElement.getElementsByTagName("PERM_LINE1").item(0).getTextContent();
                        if (PERM_LINE1 == null) {
                            PERM_LINE1 = "";
                        }


                        PERM_LINE2 = eElement.getElementsByTagName("PERM_LINE2").item(0).getTextContent();
                        if (PERM_LINE2 == null) {
                            PERM_LINE2 = "";
                        }


                        PERM_LINE3 = eElement.getElementsByTagName("PERM_LINE3").item(0).getTextContent();
                        if (PERM_LINE3 == null) {
                            PERM_LINE3 = "";
                        }


                        PERM_CITY = eElement.getElementsByTagName("PERM_CITY").item(0).getTextContent();
                        if (PERM_CITY == null) {
                            PERM_CITY = "";
                        }

                        PERM_DIST = eElement.getElementsByTagName("PERM_DIST").item(0).getTextContent();
                        if (PERM_DIST == null) {
                            PERM_DIST = "";
                        }

                        PERM_STATE = eElement.getElementsByTagName("PERM_STATE").item(0).getTextContent();
                        if (PERM_STATE == null) {
                            PERM_STATE = "";
                        }


                        PERM_COUNTRY = eElement.getElementsByTagName("PERM_COUNTRY").item(0).getTextContent();
                        if (PERM_COUNTRY == null) {
                            PERM_COUNTRY = "";
                        }


                        PERM_PIN = eElement.getElementsByTagName("PERM_PIN").item(0).getTextContent();
                        if (PERM_PIN == null) {
                            PERM_PIN = "";
                        }

                        PERM_POA = eElement.getElementsByTagName("PERM_POA").item(0).getTextContent();
                        if (PERM_POA == null) {
                            PERM_POA = "";
                        }

                        PERM_CORRES_SAMEFLAG = eElement.getElementsByTagName("PERM_CORRES_SAMEFLAG").item(0).getTextContent();
                        if (PERM_CORRES_SAMEFLAG == null) {
                            PERM_CORRES_SAMEFLAG = "";
                        }

                        CORRES_LINE1 = eElement.getElementsByTagName("CORRES_LINE1").item(0).getTextContent();
                        if (CORRES_LINE1 == null) {
                            CORRES_LINE1 = "";
                        }

                        CORRES_LINE2 = eElement.getElementsByTagName("CORRES_LINE2").item(0).getTextContent();
                        if (CORRES_LINE2 == null) {
                            CORRES_LINE2 = "";
                        }

                        CORRES_LINE3 = eElement.getElementsByTagName("CORRES_LINE3").item(0).getTextContent();
                        if (CORRES_LINE3 == null) {
                            CORRES_LINE3 = "";
                        }

                        CORRES_CITY = eElement.getElementsByTagName("CORRES_CITY").item(0).getTextContent();
                        if (CORRES_CITY == null) {
                            CORRES_CITY = "";
                        }

                        CORRES_DIST = eElement.getElementsByTagName("CORRES_DIST").item(0).getTextContent();
                        if (CORRES_DIST == null) {
                            CORRES_DIST = "";
                        }

                        CORRES_STATE = eElement.getElementsByTagName("CORRES_STATE").item(0).getTextContent();
                        if (CORRES_STATE == null) {
                            CORRES_STATE = "";
                        }

                        CORRES_COUNTRY = eElement.getElementsByTagName("CORRES_COUNTRY").item(0).getTextContent();
                        if (CORRES_COUNTRY == null) {
                            CORRES_COUNTRY = "";
                        }

                        CORRES_PIN = eElement.getElementsByTagName("CORRES_PIN").item(0).getTextContent();
                        if (CORRES_PIN == null) {
                            CORRES_PIN = "";
                        }

                        CORRES_POA = eElement.getElementsByTagName("CORRES_POA").item(0).getTextContent();
                        if (CORRES_POA == null) {
                            CORRES_POA = "";
                        }

                        RESI_STD_CODE = eElement.getElementsByTagName("RESI_STD_CODE").item(0).getTextContent();
                        if (RESI_STD_CODE == null) {
                            RESI_STD_CODE = "";
                        }

                        RESI_TEL_NUM = eElement.getElementsByTagName("RESI_TEL_NUM").item(0).getTextContent();
                        if (RESI_TEL_NUM == null) {
                            RESI_TEL_NUM = "";
                        }
                        OFF_STD_CODE = eElement.getElementsByTagName("OFF_STD_CODE").item(0).getTextContent();
                        if (OFF_STD_CODE == null) {
                            OFF_STD_CODE = "";
                        }

                        OFF_TEL_NUM = eElement.getElementsByTagName("OFF_TEL_NUM").item(0).getTextContent();
                        if (OFF_TEL_NUM == null) {
                            OFF_TEL_NUM = "";
                        }

                        MOB_NUM = eElement.getElementsByTagName("MOB_NUM").item(0).getTextContent();
                        if (MOB_NUM == null) {
                            MOB_NUM = "";
                        }

                        EMAIL = eElement.getElementsByTagName("EMAIL").item(0).getTextContent();
                        if (EMAIL == null) {
                            EMAIL = "";
                        }


                        DEC_DATE = eElement.getElementsByTagName("DEC_DATE").item(0).getTextContent();
                        if (DEC_DATE == null) {
                            DEC_DATE = "";
                        }


                        DEC_PLACE = eElement.getElementsByTagName("DEC_PLACE").item(0).getTextContent();
                        if (DEC_PLACE == null) {
                            DEC_PLACE = "";
                        }

                        String KYC_DATE = eElement.getElementsByTagName("KYC_DATE").item(0).getTextContent();
                        if (KYC_DATE == null) {
                            KYC_DATE = "";
                        }

                        String DOC_SUB = eElement.getElementsByTagName("DOC_SUB").item(0).getTextContent();
                        if (DOC_SUB == null) {
                            DOC_SUB = "";
                        }

                        String KYC_NAME = eElement.getElementsByTagName("KYC_NAME").item(0).getTextContent();
                        if (KYC_NAME == null) {
                            KYC_NAME = "";
                        }

                        //doc details
                        NodeList nList_IDENTITY_DETAILS = doc.getElementsByTagName("IDENTITY");

                        for (int temp_IDENTITY_DETAILS = 0; temp_IDENTITY_DETAILS < nList_IDENTITY_DETAILS.getLength(); temp_IDENTITY_DETAILS++) {
                            Node nNode_IDENTITY_DETAILS = nList_IDENTITY_DETAILS.item(temp_IDENTITY_DETAILS);
                            if (nNode_IDENTITY_DETAILS.getNodeType() == Node.ELEMENT_NODE) {
                                org.w3c.dom.Element eElement_IDENTITY_DETAILS = (org.w3c.dom.Element) nNode_IDENTITY_DETAILS;

                                String SEQUENCE_NO = eElement_IDENTITY_DETAILS.getElementsByTagName("SEQUENCE_NO").item(0).getTextContent();
                                if (SEQUENCE_NO == null) {
                                    SEQUENCE_NO = "";
                                }
                                if (SEQUENCE_NO.equalsIgnoreCase("1")) {


                                    IDENT_TYPE1 = eElement_IDENTITY_DETAILS.getElementsByTagName("IDENT_TYPE").item(0).getTextContent();
                                    if (IDENT_TYPE1 == null) {
                                        IDENT_TYPE1 = "";
                                    }

                                    IDENT_NUM1 = eElement_IDENTITY_DETAILS.getElementsByTagName("IDENT_NUM").item(0).getTextContent();
                                    if (IDENT_NUM1 == null) {
                                        IDENT_NUM1 = "";
                                    }
                                }
                                if (SEQUENCE_NO.equalsIgnoreCase("2")) {
                                    IDENT_TYPE2 = eElement_IDENTITY_DETAILS.getElementsByTagName("IDENT_TYPE").item(0).getTextContent();
                                    if (IDENT_TYPE2 == null) {
                                        IDENT_TYPE2 = "";
                                    }

                                    IDENT_NUM2 = eElement_IDENTITY_DETAILS.getElementsByTagName("IDENT_NUM").item(0).getTextContent();
                                    if (IDENT_NUM2 == null) {
                                        IDENT_NUM2 = "";
                                    }

                                }

                                if (SEQUENCE_NO.equalsIgnoreCase("3")) {
                                    IDENT_TYPE3 = eElement_IDENTITY_DETAILS.getElementsByTagName("IDENT_TYPE").item(0).getTextContent();
                                    if (IDENT_TYPE3 == null) {
                                        IDENT_TYPE3 = "";
                                    }

                                    IDENT_NUM3 = eElement_IDENTITY_DETAILS.getElementsByTagName("IDENT_NUM").item(0).getTextContent();
                                    if (IDENT_NUM3 == null) {
                                        IDENT_NUM3 = "";
                                    }
                                }
                                if (SEQUENCE_NO.equalsIgnoreCase("4")) {
                                    IDENT_TYPE4 = eElement_IDENTITY_DETAILS.getElementsByTagName("IDENT_TYPE").item(0).getTextContent();
                                    if (IDENT_TYPE4 == null) {
                                        IDENT_TYPE4 = "";
                                    }

                                    IDENT_NUM4 = eElement_IDENTITY_DETAILS.getElementsByTagName("IDENT_NUM").item(0).getTextContent();
                                    if (IDENT_NUM4 == null) {
                                        IDENT_NUM4 = "";
                                    }

                                }

                                if (SEQUENCE_NO.equalsIgnoreCase("5")) {
                                    IDENT_TYPE5 = eElement_IDENTITY_DETAILS.getElementsByTagName("IDENT_TYPE").item(0).getTextContent();
                                    if (IDENT_TYPE5 == null) {
                                        IDENT_TYPE5 = "";
                                    }

                                    IDENT_NUM5 = eElement_IDENTITY_DETAILS.getElementsByTagName("IDENT_NUM").item(0).getTextContent();
                                    if (IDENT_NUM5 == null) {
                                        IDENT_NUM5 = "";
                                    }

                                }

                                if (SEQUENCE_NO.equalsIgnoreCase("6")) {
                                    IDENT_TYPE6 = eElement_IDENTITY_DETAILS.getElementsByTagName("IDENT_TYPE").item(0).getTextContent();
                                    if (IDENT_TYPE6 == null) {
                                        IDENT_TYPE6 = "";
                                    }

                                    IDENT_NUM6 = eElement_IDENTITY_DETAILS.getElementsByTagName("IDENT_NUM").item(0).getTextContent();
                                    if (IDENT_NUM6 == null) {
                                        IDENT_NUM6 = "";
                                    }


                                }


                            }
                        }

                        NodeList nList_IMAGE_DETAILS = doc.getElementsByTagName("IMAGE");

                        for (int temp_IMAGE_DETAILS = 0; temp_IMAGE_DETAILS < nList_IMAGE_DETAILS.getLength(); temp_IMAGE_DETAILS++) {
                            Node nNode_IMAGE_DETAILS = nList_IMAGE_DETAILS.item(temp_IMAGE_DETAILS);
                            if (nNode_IMAGE_DETAILS.getNodeType() == Node.ELEMENT_NODE) {
                                org.w3c.dom.Element eElement_IMAGE_DETAILS = (org.w3c.dom.Element) nNode_IMAGE_DETAILS;

                                String SEQUENCE_NO = eElement_IMAGE_DETAILS.getElementsByTagName("SEQUENCE_NO").item(0).getTextContent();
                                if (SEQUENCE_NO == null) {
                                    SEQUENCE_NO = "";
                                }
                                if (SEQUENCE_NO.equalsIgnoreCase("1")) {

                                    IMAGE_TYPE1 = eElement_IMAGE_DETAILS.getElementsByTagName("IMAGE_TYPE").item(0).getTextContent();
                                    if (IMAGE_TYPE1 == null) {
                                        IMAGE_TYPE1 = "";
                                    }

                                    IMAGE_CODE1 = eElement_IMAGE_DETAILS.getElementsByTagName("IMAGE_CODE").item(0).getTextContent();
                                    if (IMAGE_CODE1 == null) {
                                        IMAGE_CODE1 = "";
                                    }

                                    IMAGE_DATA1 = eElement_IMAGE_DETAILS.getElementsByTagName("IMAGE_DATA").item(0).getTextContent();
                                    if (IMAGE_DATA1 == null) {
                                        IMAGE_DATA1 = "";
                                    }
                                    if (IMAGE_CODE1.equalsIgnoreCase("02")) {
                                        str_QR_code_Photo = IMAGE_DATA1;
                                    }

                                }
                                if (SEQUENCE_NO.equalsIgnoreCase("2")) {


                                    IMAGE_TYPE2 = eElement_IMAGE_DETAILS.getElementsByTagName("IMAGE_TYPE").item(0).getTextContent();
                                    if (IMAGE_TYPE2 == null) {
                                        IMAGE_TYPE2 = "";
                                    }

                                    IMAGE_CODE2 = eElement_IMAGE_DETAILS.getElementsByTagName("IMAGE_CODE").item(0).getTextContent();
                                    if (IMAGE_CODE2 == null) {
                                        IMAGE_CODE2 = "";
                                    }


                                    IMAGE_DATA2 = eElement_IMAGE_DETAILS.getElementsByTagName("IMAGE_DATA").item(0).getTextContent();
                                    if (IMAGE_DATA2 == null) {
                                        IMAGE_DATA2 = "";
                                    }
                                    if (IMAGE_CODE2.equalsIgnoreCase("02")) {
                                        str_QR_code_Photo = IMAGE_DATA2;
                                    }
                                }

                                if (SEQUENCE_NO.equalsIgnoreCase("3")) {
                                    IMAGE_TYPE3 = eElement_IMAGE_DETAILS.getElementsByTagName("IMAGE_TYPE").item(0).getTextContent();
                                    if (IMAGE_TYPE3 == null) {
                                        IMAGE_TYPE3 = "";
                                    }

                                    IMAGE_CODE3 = eElement_IMAGE_DETAILS.getElementsByTagName("IMAGE_CODE").item(0).getTextContent();
                                    if (IMAGE_CODE3 == null) {
                                        IMAGE_CODE3 = "";
                                    }


                                    IMAGE_DATA3 = eElement_IMAGE_DETAILS.getElementsByTagName("IMAGE_DATA").item(0).getTextContent();
                                    if (IMAGE_DATA3 == null) {
                                        IMAGE_DATA3 = "";
                                    }
                                    if (IMAGE_CODE3.equalsIgnoreCase("02")) {
                                        str_QR_code_Photo = IMAGE_DATA3;
                                    }
                                }
                                if (SEQUENCE_NO.equalsIgnoreCase("4")) {
                                    IMAGE_TYPE4 = eElement_IMAGE_DETAILS.getElementsByTagName("IMAGE_TYPE").item(0).getTextContent();
                                    if (IMAGE_TYPE4 == null) {
                                        IMAGE_TYPE4 = "";
                                    }

                                    IMAGE_CODE4 = eElement_IMAGE_DETAILS.getElementsByTagName("IMAGE_CODE").item(0).getTextContent();
                                    if (IMAGE_CODE4 == null) {
                                        IMAGE_CODE4 = "";
                                    }

                                    IMAGE_DATA4 = eElement_IMAGE_DETAILS.getElementsByTagName("IMAGE_DATA").item(0).getTextContent();
                                    if (IMAGE_DATA4 == null) {
                                        IMAGE_DATA4 = "";
                                    }
                                    if (IMAGE_CODE4.equalsIgnoreCase("02")) {
                                        str_QR_code_Photo = IMAGE_DATA4;
                                    }
                                }

                                if (SEQUENCE_NO.equalsIgnoreCase("5")) {
                                    IMAGE_TYPE5 = eElement_IMAGE_DETAILS.getElementsByTagName("IMAGE_TYPE").item(0).getTextContent();
                                    if (IMAGE_TYPE5 == null) {
                                        IMAGE_TYPE5 = "";
                                    }

                                    IMAGE_CODE5 = eElement_IMAGE_DETAILS.getElementsByTagName("IMAGE_CODE").item(0).getTextContent();
                                    if (IMAGE_CODE5 == null) {
                                        IMAGE_CODE5 = "";
                                    }


                                    IMAGE_DATA5 = eElement_IMAGE_DETAILS.getElementsByTagName("IMAGE_DATA").item(0).getTextContent();
                                    if (IMAGE_DATA5 == null) {
                                        IMAGE_DATA5 = "";
                                    }
                                    if (IMAGE_CODE5.equalsIgnoreCase("02")) {
                                        str_QR_code_Photo = IMAGE_DATA5;
                                    }
                                }


                                if (SEQUENCE_NO.equalsIgnoreCase("6")) {
                                    IMAGE_TYPE6 = eElement_IMAGE_DETAILS.getElementsByTagName("IMAGE_TYPE").item(0).getTextContent();
                                    if (IMAGE_TYPE6 == null) {
                                        IMAGE_TYPE6 = "";
                                    }

                                    IMAGE_CODE6 = eElement_IMAGE_DETAILS.getElementsByTagName("IMAGE_CODE").item(0).getTextContent();
                                    if (IMAGE_CODE6 == null) {
                                        IMAGE_CODE6 = "";
                                    }


                                    IMAGE_DATA6 = eElement_IMAGE_DETAILS.getElementsByTagName("IMAGE_DATA").item(0).getTextContent();
                                    if (IMAGE_DATA6 == null) {
                                        IMAGE_DATA6 = "";
                                    }
                                    if (IMAGE_CODE6.equalsIgnoreCase("02")) {
                                        str_QR_code_Photo = IMAGE_DATA6;
                                    }
                                }

                            }
                        }

                    }
                }

                /*Single.fromCallable(() -> CreateCKYCPDF())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(@NonNull Boolean aBoolean) throws Exception {
                                if (!IMAGE_CODE1.equalsIgnoreCase("")) {

                                    mStorageUtils.saveBase64ToFile(mContext, StorageUtils.DIRECT_DIRECTORY, IMAGE_CODE1,
                                            str_CKYC_PAN_no + "_CKYC_" + IMAGE_CODE1 + "." + IMAGE_TYPE1);

                                }

                                if (!IMAGE_CODE2.equalsIgnoreCase("")) {

                                    mStorageUtils.saveBase64ToFile(mContext, StorageUtils.DIRECT_DIRECTORY, IMAGE_CODE2,
                                            str_CKYC_PAN_no + "_CKYC_" + IMAGE_CODE2 + "." + IMAGE_TYPE2);
                                }
                                if (!IMAGE_CODE3.equalsIgnoreCase("")) {

                                    mStorageUtils.saveBase64ToFile(mContext, StorageUtils.DIRECT_DIRECTORY, IMAGE_CODE3,
                                            str_CKYC_PAN_no + "_CKYC_" + IMAGE_CODE3 + "." + IMAGE_TYPE3);
                                }
                                if (!IMAGE_CODE4.equalsIgnoreCase("")) {

                                    mStorageUtils.saveBase64ToFile(mContext, StorageUtils.DIRECT_DIRECTORY, IMAGE_CODE4,
                                            str_CKYC_PAN_no + "_CKYC_" + IMAGE_CODE4 + "." + IMAGE_TYPE4);
                                }
                            }
                        }, throwable -> {
                            mCommonMethods.showToast(mContext, "File Not Found");
                        });*/

                if (!IMAGE_CODE1.equalsIgnoreCase("")) {
                    mStorageUtils.saveBase64ToFile(mContext, StorageUtils.DIRECT_DIRECTORY, IMAGE_CODE1,
                            str_CKYC_PAN_no + "_CKYC_" + IMAGE_CODE1 + "." + IMAGE_TYPE1);
                }
                if (!IMAGE_CODE2.equalsIgnoreCase("")) {
                    mStorageUtils.saveBase64ToFile(mContext, StorageUtils.DIRECT_DIRECTORY, IMAGE_CODE2,
                            str_CKYC_PAN_no + "_CKYC_" + IMAGE_CODE2 + "." + IMAGE_TYPE2);
                }
                if (!IMAGE_CODE3.equalsIgnoreCase("")) {
                    mStorageUtils.saveBase64ToFile(mContext, StorageUtils.DIRECT_DIRECTORY, IMAGE_CODE3,
                            str_CKYC_PAN_no + "_CKYC_" + IMAGE_CODE3 + "." + IMAGE_TYPE3);
                }
                if (!IMAGE_CODE4.equalsIgnoreCase("")) {
                    mStorageUtils.saveBase64ToFile(mContext, StorageUtils.DIRECT_DIRECTORY, IMAGE_CODE4,
                            str_CKYC_PAN_no + "_CKYC_" + IMAGE_CODE4 + "." + IMAGE_TYPE4);
                }
            } catch (Exception e) {
                mCommonMethods.printLog("Integrate Service", e
                        + "Error  in getting in Login Table");
                e.printStackTrace();
                isRunning = false;
            }
        } else {
            isRunning = false;
        }

        return isRunning;
    }

    private boolean CreateCKYCPDF() {

        boolean isRunning = true;

        try {
            Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 11,
                    Font.BOLD, BaseColor.WHITE);
            Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.BOLD);
            Font small_bold_for_name = new Font(Font.FontFamily.TIMES_ROMAN,
                    10, Font.BOLD);
            Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                    Font.NORMAL);
            Font sub_headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 7,
                    Font.BOLD);

            File mypath = mStorageUtils.createFileToAppSpecificDir(mContext,
                    str_CKYC_PAN_no + "_X1.pdf");

            Rectangle rect = new Rectangle(594f, 792f);

            Document document = new Document(rect, 50, 50, 50, 50);
            // Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter pdf_writer = PdfWriter.getInstance(document, new FileOutputStream(
                    mypath.getPath()));

            document.open();
            // For SBI- Life Logo starts
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sbi_life_logo);
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
            PdfPTable table_proposal_number = new PdfPTable(2);
            table_proposal_number.setWidthPercentage(100);


           /* PdfPCell Proposal_number_cell_1 = new PdfPCell(new Paragraph(
                    "Proposal Number", small_normal));
            PdfPCell Proposal_number_cell_2 = new PdfPCell(new Paragraph(
                    ProposerNumber, small_bold));
            Proposal_number_cell_1
                    .setHorizontalAlignment(Element.ALIGN_CENTER);

            Proposal_number_cell_2
                    .setVerticalAlignment(Element.ALIGN_CENTER);


            Proposal_number_cell_1.setPadding(5);
            Proposal_number_cell_2.setPadding(5);


            table_proposal_number.addCell(Proposal_number_cell_1);
            table_proposal_number.addCell(Proposal_number_cell_2);

            document.add(table_proposal_number);
*/

            document.add(para_img_logo_after_space_1);

            PdfPTable headertable = new PdfPTable(1);
            headertable.setWidthPercentage(100);
            headertable.setHorizontalAlignment(Element.ALIGN_CENTER);
            Paragraph para_address = new Paragraph("CKYC Details", small_bold_for_name);
            para_address.setAlignment(Element.ALIGN_CENTER);
            document.add(headertable);
            // document.add(new LineSeparator());
            document.add(para_img_logo_after_space_1);
            document.add(para_address);
            document.add(para_img_logo_after_space_1);

            if (str_QR_code_Photo != null && !str_QR_code_Photo.equals("")) {
                ByteArrayOutputStream stream_photo = new ByteArrayOutputStream();
                byte[] fbyt_photo = Base64.decode(
                        str_QR_code_Photo, 0);
                Bitmap bitmap_photo = BitmapFactory
                        .decodeByteArray(fbyt_photo, 0,
                                fbyt_photo.length);
                if (bitmap_photo != null) {
                    bitmap_photo.compress(Bitmap.CompressFormat.PNG, 100, stream_photo);
                    Image img_photo = Image.getInstance(stream_photo.toByteArray());
                    img_photo.setAlignment(Image.RIGHT);
                    img_photo.getSpacingAfter();
                    img_photo.scaleToFit(200, 100);
                    Paragraph para_photo = new Paragraph("");
                    para_photo.add(img_photo);
                    document.add(para_photo);
                }
            }

            PdfPTable table_ref_no = new PdfPTable(2);
            table_ref_no.setWidthPercentage(100);

            PdfPCell ref_no_cell_1 = new PdfPCell(new Paragraph("CKYC Number", small_normal));
            PdfPCell ref_no_cell_2 = new PdfPCell(new Paragraph(CKYC_NO, small_bold));
            ref_no_cell_1.setPadding(5);
            ref_no_cell_2.setPadding(5);
            ref_no_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_ref_no.addCell(ref_no_cell_1);
            table_ref_no.addCell(ref_no_cell_2);
            document.add(table_ref_no);

            PdfPTable table_Name = new PdfPTable(2);
            table_Name.setWidthPercentage(100);

            PdfPCell Name_cell_1 = new PdfPCell(new Paragraph("Name", small_normal));
            PdfPCell Name_cell_2 = new PdfPCell(new Paragraph(FULLNAME, small_bold));
            Name_cell_1.setPadding(5);
            Name_cell_2.setPadding(5);
            Name_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_Name.addCell(Name_cell_1);
            table_Name.addCell(Name_cell_2);
            document.add(table_Name);


            PdfPTable tablemainden_name = new PdfPTable(2);
            tablemainden_name.setWidthPercentage(100);

            PdfPCell Maiden_Name_cell_1 = new PdfPCell(new Paragraph("Maiden Name", small_normal));
            PdfPCell Maiden_Name_cell_2 = new PdfPCell(new Paragraph(MAIDEN_FULLNAME, small_bold));
            Maiden_Name_cell_1.setPadding(5);
            Maiden_Name_cell_2.setPadding(5);
            Maiden_Name_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
            tablemainden_name.addCell(Maiden_Name_cell_1);
            tablemainden_name.addCell(Maiden_Name_cell_2);
            document.add(tablemainden_name);


            if (FATHERSPOUSE_FLAG.equalsIgnoreCase("01")) {
                PdfPTable tableFather_name = new PdfPTable(2);
                tableFather_name.setWidthPercentage(100);

                PdfPCell Father_cell_1 = new PdfPCell(new Paragraph("Father Name", small_normal));
                PdfPCell Father_cell_2 = new PdfPCell(new Paragraph(FATHER_FULLNAME, small_bold));
                Father_cell_1.setPadding(5);
                Father_cell_2.setPadding(5);
                Father_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableFather_name.addCell(Father_cell_1);
                tableFather_name.addCell(Father_cell_2);
                document.add(tableFather_name);
            } else {
                PdfPTable tableFather_name = new PdfPTable(2);
                tableFather_name.setWidthPercentage(100);

                PdfPCell Father_cell_1 = new PdfPCell(new Paragraph("Spouse Name", small_normal));
                PdfPCell Father_cell_2 = new PdfPCell(new Paragraph(FATHER_FULLNAME, small_bold));
                Father_cell_1.setPadding(5);
                Father_cell_2.setPadding(5);
                Father_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableFather_name.addCell(Father_cell_1);
                tableFather_name.addCell(Father_cell_2);
                document.add(tableFather_name);
            }


            PdfPTable tableMonther_name = new PdfPTable(2);
            tableMonther_name.setWidthPercentage(100);

            PdfPCell Monther_cell_1 = new PdfPCell(new Paragraph("Mother Name", small_normal));
            PdfPCell Monther_cell_2 = new PdfPCell(new Paragraph(MOTHER_FULLNAME, small_bold));
            Monther_cell_1.setPadding(5);
            Monther_cell_2.setPadding(5);
            Monther_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableMonther_name.addCell(Monther_cell_1);
            tableMonther_name.addCell(Monther_cell_2);
            document.add(tableMonther_name);


            PdfPTable tableGender = new PdfPTable(2);
            tableGender.setWidthPercentage(100);

            PdfPCell Gender_cell_1 = new PdfPCell(new Paragraph("Gender", small_normal));
            PdfPCell Gender_cell_2 = new PdfPCell(new Paragraph(GENDER, small_bold));
            Gender_cell_1.setPadding(5);
            Gender_cell_2.setPadding(5);
            Gender_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableGender.addCell(Gender_cell_1);
            tableGender.addCell(Gender_cell_2);
            document.add(tableGender);


            PdfPTable tableDOB_name = new PdfPTable(2);
            tableDOB_name.setWidthPercentage(100);

            PdfPCell DOB_cell_1 = new PdfPCell(new Paragraph("DOB", small_normal));
            PdfPCell DOB_cell_2 = new PdfPCell(new Paragraph(DOB, small_bold));
            DOB_cell_1.setPadding(5);
            DOB_cell_2.setPadding(5);
            DOB_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableDOB_name.addCell(DOB_cell_1);
            tableDOB_name.addCell(DOB_cell_2);
            document.add(tableDOB_name);


            PdfPTable tablePAN_name = new PdfPTable(2);
            tablePAN_name.setWidthPercentage(100);

            PdfPCell PAN_cell_1 = new PdfPCell(new Paragraph("PAN", small_normal));
            PdfPCell PAN_cell_2 = new PdfPCell(new Paragraph(PAN, small_bold));
            PAN_cell_1.setPadding(5);
            PAN_cell_2.setPadding(5);
            PAN_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
            tablePAN_name.addCell(PAN_cell_1);
            tablePAN_name.addCell(PAN_cell_2);
            document.add(tablePAN_name);


            PdfPTable tableForm60_name = new PdfPTable(2);
            tableForm60_name.setWidthPercentage(100);

            PdfPCell Form60_cell_1 = new PdfPCell(new Paragraph("FORM_SIXTY", small_normal));
            PdfPCell Form60_cell_2 = new PdfPCell(new Paragraph(FORM_SIXTY, small_bold));
            Form60_cell_1.setPadding(5);
            Form60_cell_2.setPadding(5);
            Form60_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableForm60_name.addCell(Form60_cell_1);
            tableForm60_name.addCell(Form60_cell_2);
            document.add(tableForm60_name);

            String proposer_Permanent_Address =
                    PERM_LINE1 + " "
                            + PERM_LINE2 + " "
                            + PERM_LINE3 + " "
                            + PERM_PIN + " "
                            + PERM_CITY + " " + PERM_DIST + " "
                            + PERM_STATE;

            PdfPTable tablepermanent_address_name = new PdfPTable(2);
            tablepermanent_address_name.setWidthPercentage(100);

            PdfPCell permanent_address_cell_1 = new PdfPCell(new Paragraph("Permanent Address", small_normal));
            PdfPCell permanent_address_cell_2 = new PdfPCell(new Paragraph(proposer_Permanent_Address, small_bold));
            permanent_address_cell_1.setPadding(5);
            permanent_address_cell_2.setPadding(5);
            permanent_address_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
            tablepermanent_address_name.addCell(permanent_address_cell_1);
            tablepermanent_address_name.addCell(permanent_address_cell_2);
            document.add(tablepermanent_address_name);


            PdfPTable tablepermanent_address_country_name = new PdfPTable(2);
            tablepermanent_address_country_name.setWidthPercentage(100);

            PdfPCell permanent_address_country_cell_1 = new PdfPCell(new Paragraph("Permanent Address Country", small_normal));
            PdfPCell permanent_address_country_cell_2 = new PdfPCell(new Paragraph(PERM_COUNTRY, small_bold));
            permanent_address_country_cell_1.setPadding(5);
            permanent_address_country_cell_2.setPadding(5);
            permanent_address_country_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
            tablepermanent_address_country_name.addCell(permanent_address_country_cell_1);
            tablepermanent_address_country_name.addCell(permanent_address_country_cell_2);
            document.add(tablepermanent_address_country_name);


            PdfPTable tablepermanent_address_same_com_name = new PdfPTable(2);
            tablepermanent_address_same_com_name.setWidthPercentage(100);

            PdfPCell permanent_address_same_com_cell_1 = new PdfPCell(new Paragraph("Correspondence Address same as Permanent Address", small_normal));
            PdfPCell permanent_address_same_com_cell_2 = new PdfPCell(new Paragraph(PERM_CORRES_SAMEFLAG, small_bold));
            permanent_address_same_com_cell_1.setPadding(5);
            permanent_address_same_com_cell_2.setPadding(5);
            permanent_address_same_com_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
            tablepermanent_address_same_com_name.addCell(permanent_address_same_com_cell_1);
            tablepermanent_address_same_com_name.addCell(permanent_address_same_com_cell_2);
            document.add(tablepermanent_address_same_com_name);


            String proposer_Correspondence_Address =
                    CORRES_LINE1 + " "
                            + CORRES_LINE2 + " "
                            + CORRES_LINE3 + " "
                            + CORRES_PIN + " "
                            + CORRES_CITY + " " + CORRES_DIST + " "
                            + CORRES_STATE;

            PdfPTable tablecorrespondece_address_name = new PdfPTable(2);
            tablecorrespondece_address_name.setWidthPercentage(100);

            PdfPCell correspondece_address_cell_1 = new PdfPCell(new Paragraph("Correspondence Address", small_normal));
            PdfPCell correspondece_address_cell_2 = new PdfPCell(new Paragraph(proposer_Correspondence_Address, small_bold));
            correspondece_address_cell_1.setPadding(5);
            correspondece_address_cell_2.setPadding(5);
            correspondece_address_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
            tablecorrespondece_address_name.addCell(correspondece_address_cell_1);
            tablecorrespondece_address_name.addCell(correspondece_address_cell_2);
            document.add(tablecorrespondece_address_name);

            PdfPTable tablecorrespondece_address_country_name = new PdfPTable(2);
            tablecorrespondece_address_country_name.setWidthPercentage(100);

            PdfPCell correspondece_address_country_cell_1 = new PdfPCell(new Paragraph(
                    "Correspondence Address Country", small_normal));
            PdfPCell correspondece_address_country_cell_2 = new PdfPCell(new Paragraph(CORRES_COUNTRY,
                    small_bold));
            correspondece_address_country_cell_1.setPadding(5);
            correspondece_address_country_cell_2.setPadding(5);
            correspondece_address_country_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
            tablecorrespondece_address_country_name.addCell(correspondece_address_country_cell_1);
            tablecorrespondece_address_country_name.addCell(correspondece_address_country_cell_2);
            document.add(tablecorrespondece_address_country_name);


            PdfPTable tableresi_tel_no_name = new PdfPTable(2);
            tableresi_tel_no_name.setWidthPercentage(100);

            PdfPCell resi_tel_no_cell_1 = new PdfPCell(new Paragraph("Resi. tel. number",
                    small_normal));
            PdfPCell resi_tel_no_cell_2 = new PdfPCell(new Paragraph(RESI_STD_CODE + " "
                    + RESI_TEL_NUM, small_bold));
            resi_tel_no_cell_1.setPadding(5);
            resi_tel_no_cell_2.setPadding(5);
            resi_tel_no_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableresi_tel_no_name.addCell(resi_tel_no_cell_1);
            tableresi_tel_no_name.addCell(resi_tel_no_cell_2);
            document.add(tableresi_tel_no_name);


            PdfPTable tableoff_tel_no_name = new PdfPTable(2);
            tableoff_tel_no_name.setWidthPercentage(100);

            PdfPCell off_tel_no_cell_1 = new PdfPCell(new Paragraph("Office tel. Number",
                    small_normal));
            PdfPCell off_tel_no_cell_2 = new PdfPCell(new Paragraph(OFF_STD_CODE + " " + OFF_TEL_NUM,
                    small_bold));
            off_tel_no_cell_1.setPadding(5);
            off_tel_no_cell_2.setPadding(5);
            off_tel_no_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableoff_tel_no_name.addCell(off_tel_no_cell_1);
            tableoff_tel_no_name.addCell(off_tel_no_cell_2);
            document.add(tableoff_tel_no_name);


            PdfPTable tableMob_no_no_name = new PdfPTable(2);
            tableMob_no_no_name.setWidthPercentage(100);

            PdfPCell Mob_no_no_cell_1 = new PdfPCell(new Paragraph("Mobile Number", small_normal));
            PdfPCell Mob_no_no_cell_2 = new PdfPCell(new Paragraph(MOB_NUM, small_bold));
            Mob_no_no_cell_1.setPadding(5);
            Mob_no_no_cell_2.setPadding(5);
            Mob_no_no_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableMob_no_no_name.addCell(Mob_no_no_cell_1);
            tableMob_no_no_name.addCell(Mob_no_no_cell_2);
            document.add(tableMob_no_no_name);


            PdfPTable tableEmail_Id_name = new PdfPTable(2);
            tableEmail_Id_name.setWidthPercentage(100);

            PdfPCell Email_Id_cell_1 = new PdfPCell(new Paragraph("Email_Id", small_normal));
            PdfPCell Email_Id_cell_2 = new PdfPCell(new Paragraph(EMAIL, small_bold));
            Email_Id_cell_1.setPadding(5);
            Email_Id_cell_2.setPadding(5);
            Email_Id_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableEmail_Id_name.addCell(Email_Id_cell_1);
            tableEmail_Id_name.addCell(Email_Id_cell_2);
            document.add(tableEmail_Id_name);


            PdfPTable tabledec_date_name = new PdfPTable(2);
            tabledec_date_name.setWidthPercentage(100);

            PdfPCell dec_date_cell_1 = new PdfPCell(new Paragraph("Dec date", small_normal));
            PdfPCell dec_date_cell_2 = new PdfPCell(new Paragraph(DEC_DATE, small_bold));
            dec_date_cell_1.setPadding(5);
            dec_date_cell_2.setPadding(5);
            dec_date_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabledec_date_name.addCell(dec_date_cell_1);
            tabledec_date_name.addCell(dec_date_cell_2);
            document.add(tabledec_date_name);


            PdfPTable tabledec_place_name = new PdfPTable(2);
            tabledec_place_name.setWidthPercentage(100);

            PdfPCell dec_place_cell_1 = new PdfPCell(new Paragraph("Dec Place", small_normal));
            PdfPCell dec_place_cell_2 = new PdfPCell(new Paragraph(DEC_PLACE, small_bold));
            dec_place_cell_1.setPadding(5);
            dec_place_cell_2.setPadding(5);
            dec_place_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabledec_place_name.addCell(dec_place_cell_1);
            tabledec_place_name.addCell(dec_place_cell_2);
            document.add(tabledec_place_name);


            String Idetification_type1 = "";
            if (IDENT_TYPE1.contains("A")) {
                Idetification_type1 = "Passport Number";
            } else if (IDENT_TYPE1.contains("D")) {
                Idetification_type1 = "Driving License Number";
            } else if (IDENT_TYPE1.contains("C")) {
                Idetification_type1 = "Pan Card Number";
            } else if (IDENT_TYPE1.contains("E")) {
                Idetification_type1 = "Aadhar Card Nummber";
            } else if (IDENT_TYPE1.contains("B")) {
                Idetification_type1 = "Voter s Identity Card Number";
            } else {
                Idetification_type1 = IDENT_TYPE1;
            }
            if (!IDENT_TYPE1.equalsIgnoreCase("")) {
                PdfPTable tableIdetification_type1_name = new PdfPTable(2);
                tableIdetification_type1_name.setWidthPercentage(100);

                PdfPCell Idetification_type1_cell_1 = new PdfPCell(new Paragraph(Idetification_type1,
                        small_normal));
                PdfPCell Idetification_type1_cell_2 = new PdfPCell(new Paragraph(IDENT_NUM1, small_bold));
                Idetification_type1_cell_1.setPadding(5);
                Idetification_type1_cell_2.setPadding(5);
                Idetification_type1_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableIdetification_type1_name.addCell(Idetification_type1_cell_1);
                tableIdetification_type1_name.addCell(Idetification_type1_cell_2);
                document.add(tableIdetification_type1_name);
            }


            String Idetification_type2 = "";
            if (IDENT_TYPE2.contains("A")) {
                Idetification_type2 = "Passport Number";
            } else if (IDENT_TYPE2.contains("D")) {
                Idetification_type2 = "Driving License Number";
            } else if (IDENT_TYPE2.contains("C")) {
                Idetification_type2 = "Pan Card Number";
            } else if (IDENT_TYPE2.contains("E")) {
                Idetification_type2 = "Aadhar Card Nummber";
            } else if (IDENT_TYPE2.contains("B")) {
                Idetification_type2 = "Voter s Identity Card Number";
            } else {
                Idetification_type2 = IDENT_TYPE2;
            }
            if (!IDENT_TYPE2.equalsIgnoreCase("")) {
                PdfPTable tableIdetification_type2_name = new PdfPTable(2);
                tableIdetification_type2_name.setWidthPercentage(100);

                PdfPCell Idetification_type2_cell_1 = new PdfPCell(new Paragraph(Idetification_type2,
                        small_normal));
                PdfPCell Idetification_type2_cell_2 = new PdfPCell(new Paragraph(IDENT_NUM2, small_bold));
                Idetification_type2_cell_1.setPadding(5);
                Idetification_type2_cell_2.setPadding(5);
                Idetification_type2_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableIdetification_type2_name.addCell(Idetification_type2_cell_1);
                tableIdetification_type2_name.addCell(Idetification_type2_cell_2);
                document.add(tableIdetification_type2_name);
            }


            String Idetification_type3 = "";
            if (IDENT_TYPE3.contains("A")) {
                Idetification_type3 = "Passport Number";
            } else if (IDENT_TYPE3.contains("D")) {
                Idetification_type3 = "Driving License Number";
            } else if (IDENT_TYPE3.contains("C")) {
                Idetification_type3 = "Pan Card Number";
            } else if (IDENT_TYPE3.contains("E")) {
                Idetification_type3 = "Aadhar Card Nummber";
            } else if (IDENT_TYPE3.contains("B")) {
                Idetification_type3 = "Voter s Identity Card Number";
            } else {
                Idetification_type3 = IDENT_TYPE3;
            }
            if (!IDENT_TYPE3.equalsIgnoreCase("")) {
                PdfPTable tableIdetification_type3_name = new PdfPTable(2);
                tableIdetification_type3_name.setWidthPercentage(100);

                PdfPCell Idetification_type3_cell_1 = new PdfPCell(new Paragraph(Idetification_type3,
                        small_normal));
                PdfPCell Idetification_type3_cell_2 = new PdfPCell(new Paragraph(IDENT_NUM3, small_bold));
                Idetification_type3_cell_1.setPadding(5);
                Idetification_type3_cell_2.setPadding(5);
                Idetification_type3_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableIdetification_type3_name.addCell(Idetification_type3_cell_1);
                tableIdetification_type3_name.addCell(Idetification_type3_cell_2);
                document.add(tableIdetification_type3_name);
            }

            String Idetification_type4 = "";
            if (IDENT_TYPE4.contains("A")) {
                Idetification_type4 = "Passport Number";
            } else if (IDENT_TYPE4.contains("D")) {
                Idetification_type4 = "Driving License Number";
            } else if (IDENT_TYPE4.contains("C")) {
                Idetification_type4 = "Pan Card Number";
            } else if (IDENT_TYPE4.contains("E")) {
                Idetification_type4 = "Aadhar Card Nummber";
            } else if (IDENT_TYPE4.contains("B")) {
                Idetification_type4 = "Voter s Identity Card Number";
            } else {
                Idetification_type4 = IDENT_TYPE4;
            }
            if (!IDENT_TYPE4.equalsIgnoreCase("")) {
                PdfPTable tableIdetification_type4_name = new PdfPTable(2);
                tableIdetification_type4_name.setWidthPercentage(100);

                PdfPCell Idetification_type4_cell_1 = new PdfPCell(new Paragraph(Idetification_type4,
                        small_normal));
                PdfPCell Idetification_type4_cell_2 = new PdfPCell(new Paragraph(IDENT_NUM4, small_bold));
                Idetification_type4_cell_1.setPadding(5);
                Idetification_type4_cell_2.setPadding(5);
                Idetification_type4_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableIdetification_type4_name.addCell(Idetification_type4_cell_1);
                tableIdetification_type4_name.addCell(Idetification_type4_cell_2);
                document.add(tableIdetification_type4_name);
            }


            String Idetification_type5 = "";
            if (IDENT_TYPE5.contains("A")) {
                Idetification_type5 = "Passport Number";
            } else if (IDENT_TYPE5.contains("D")) {
                Idetification_type5 = "Driving License Number";
            } else if (IDENT_TYPE5.contains("C")) {
                Idetification_type5 = "Pan Card Number";
            } else if (IDENT_TYPE5.contains("E")) {
                Idetification_type5 = "Aadhar Card Nummber";
            } else if (IDENT_TYPE5.contains("B")) {
                Idetification_type5 = "Voter s Identity Card Number";
            } else {
                Idetification_type5 = IDENT_TYPE5;
            }
            if (!IDENT_TYPE5.equalsIgnoreCase("")) {
                PdfPTable tableIdetification_type5_name = new PdfPTable(2);
                tableIdetification_type5_name.setWidthPercentage(100);

                PdfPCell Idetification_type5_cell_1 = new PdfPCell(new Paragraph(Idetification_type5,
                        small_normal));
                PdfPCell Idetification_type5_cell_2 = new PdfPCell(new Paragraph(IDENT_NUM5, small_bold));
                Idetification_type5_cell_1.setPadding(5);
                Idetification_type5_cell_2.setPadding(5);
                Idetification_type5_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableIdetification_type5_name.addCell(Idetification_type5_cell_1);
                tableIdetification_type5_name.addCell(Idetification_type5_cell_2);
                document.add(tableIdetification_type5_name);
            }

            String Idetification_type6 = "";
            if (IDENT_TYPE6.contains("A")) {
                Idetification_type6 = "Passport Number";
            } else if (IDENT_TYPE6.contains("D")) {
                Idetification_type6 = "Driving License Number";
            } else if (IDENT_TYPE6.contains("C")) {
                Idetification_type6 = "Pan Card Number";
            } else if (IDENT_TYPE6.contains("E")) {
                Idetification_type6 = "Aadhar Card Nummber";
            } else if (IDENT_TYPE6.contains("B")) {
                Idetification_type6 = "Voter s Identity Card Number";
            } else {
                Idetification_type6 = IDENT_TYPE6;
            }
            if (!IDENT_TYPE6.equalsIgnoreCase("")) {
                PdfPTable tableIdetification_type6_name = new PdfPTable(2);
                tableIdetification_type6_name.setWidthPercentage(100);

                PdfPCell Idetification_type6_cell_1 = new PdfPCell(new Paragraph(Idetification_type6,
                        small_normal));
                PdfPCell Idetification_type6_cell_2 = new PdfPCell(new Paragraph(IDENT_NUM6, small_bold));
                Idetification_type6_cell_1.setPadding(5);
                Idetification_type6_cell_2.setPadding(5);
                Idetification_type6_cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableIdetification_type6_name.addCell(Idetification_type6_cell_1);
                tableIdetification_type6_name.addCell(Idetification_type6_cell_2);
                document.add(tableIdetification_type6_name);
            }
            document.close();

        } catch (Exception e) {
            isRunning = false;
            mCommonMethods.printLog("Error", e + "Error in creating pdf");
        }
        return isRunning;
    }

    public String createFormatedCKYCDetails() {
        strFormatedCKYCDetails = "";

        strFormatedCKYCDetails = "<p style='text-align: center;'><b>CKYC Details</b></p>"
                + "<pre style='text-align: justify;'><b>CKYC Number : </b>" + CKYC_NO + "</pre><br/>"
                + "<pre style='text-align: justify;'><b>Name : </b>" + FULLNAME + "</pre><br/>"
                + "<pre style='text-align: justify;'><b>Maiden Name : </b>" + MAIDEN_FULLNAME + "</pre><br/>";
        if (FATHERSPOUSE_FLAG.equalsIgnoreCase("01")) {
            strFormatedCKYCDetails += "<pre><b>Father Name : </b>" + FATHER_FULLNAME + "</pre><br/>";
        } else {
            strFormatedCKYCDetails += "<pre><b>Spouse Name : </b>" + FATHER_FULLNAME + "</pre><br/>";
        }
        strFormatedCKYCDetails += "<pre><b>Mother Name : </b>" + MOTHER_FULLNAME + "</pre><br/>"
                + "<pre><b>Gender : </b>" + GENDER + "</pre><br/>"
                + "<pre><b>DOB : </b>" + DOB + "</pre><br/>"
                + "<pre><b>PAN : </b>" + PAN + "</pre><br/>"
                + "<pre><b>FORM SIXTY : </b>" + FORM_SIXTY + "</pre><br/>"
                + "<pre><b>Permanent Address : </b>" + PERM_LINE1 + " " + PERM_LINE2 + " " + PERM_LINE3 + " "
                + PERM_PIN + " " + PERM_CITY + " " + PERM_DIST + " " + PERM_STATE + "</pre><br/>"
                + "<pre><b>Permanent Address Country : </b>" + PERM_COUNTRY + "</pre><br/>"
                + "<pre><b>Correspondence Address same as Permanent Address : </b>" + PERM_CORRES_SAMEFLAG + "</pre><br/>"
                + "<pre><b>Correspondence Address : </b>" + CORRES_LINE1 + " " + CORRES_LINE2 + " "
                + CORRES_LINE3 + " " + CORRES_PIN + " " + CORRES_CITY + " " + CORRES_DIST + " "
                + CORRES_STATE + "</pre><br/>"
                + "<pre><b>Correspondence Address Country : </b>" + CORRES_COUNTRY + "</pre><br/>"
                + "<pre><b>Resi. Tel. Number : </b>" + RESI_STD_CODE + " " + RESI_TEL_NUM + "</pre><br/>"
                + "<pre><b>Office Tel. NUmber : </b>" + OFF_STD_CODE + " " + OFF_TEL_NUM + "</pre><br/>"
                + "<pre><b>Mobile Number : </b>" + MOB_NUM + "</pre><br/>"
                + "<pre><b>Email ID : </b>" + EMAIL + "</pre><br/>"
                + "<pre><b>Dec Date : </b>" + DEC_DATE + "</pre><br/>"
                + "<pre><b>Dec Place : </b>" + DEC_PLACE + "</pre><br/>";

        String Idetification_type1 = "";
        if (IDENT_TYPE1.contains("A")) {
            Idetification_type1 = "Passport Number";
        } else if (IDENT_TYPE1.contains("D")) {
            Idetification_type1 = "Driving License Number";
        } else if (IDENT_TYPE1.contains("C")) {
            Idetification_type1 = "Pan Card Number";
        } else if (IDENT_TYPE1.contains("E")) {
            Idetification_type1 = "Aadhar Card Nummber";
        } else if (IDENT_TYPE1.contains("B")) {
            Idetification_type1 = "Voter s Identity Card Number";
        } else {
            Idetification_type1 = IDENT_TYPE1;
        }
        if (!IDENT_TYPE1.equalsIgnoreCase("")) {
            strFormatedCKYCDetails += "<pre><b>" + Idetification_type1 + " : </b>" + IDENT_NUM1 + "</pre><br/>";
        }


        String Idetification_type2 = "";
        if (IDENT_TYPE2.contains("A")) {
            Idetification_type2 = "Passport Number";
        } else if (IDENT_TYPE2.contains("D")) {
            Idetification_type2 = "Driving License Number";
        } else if (IDENT_TYPE2.contains("C")) {
            Idetification_type2 = "Pan Card Number";
        } else if (IDENT_TYPE2.contains("E")) {
            Idetification_type2 = "Aadhar Card Nummber";
        } else if (IDENT_TYPE2.contains("B")) {
            Idetification_type2 = "Voter s Identity Card Number";
        } else {
            Idetification_type2 = IDENT_TYPE2;
        }

        if (!IDENT_TYPE2.equalsIgnoreCase("")) {
            strFormatedCKYCDetails += "<pre><b>" + Idetification_type2 + " : </b>" + IDENT_NUM2 + "</pre><br/>";
        }


        String Idetification_type3 = "";
        if (IDENT_TYPE3.contains("A")) {
            Idetification_type3 = "Passport Number";
        } else if (IDENT_TYPE3.contains("D")) {
            Idetification_type3 = "Driving License Number";
        } else if (IDENT_TYPE3.contains("C")) {
            Idetification_type3 = "Pan Card Number";
        } else if (IDENT_TYPE3.contains("E")) {
            Idetification_type3 = "Aadhar Card Nummber";
        } else if (IDENT_TYPE3.contains("B")) {
            Idetification_type3 = "Voter s Identity Card Number";
        } else {
            Idetification_type3 = IDENT_TYPE3;
        }
        if (!IDENT_TYPE3.equalsIgnoreCase("")) {
            strFormatedCKYCDetails += "<pre><b>" + Idetification_type3 + " : </b>" + IDENT_NUM3 + "</pre><br/>";
        }

        String Idetification_type4 = "";
        if (IDENT_TYPE4.contains("A")) {
            Idetification_type4 = "Passport Number";
        } else if (IDENT_TYPE4.contains("D")) {
            Idetification_type4 = "Driving License Number";
        } else if (IDENT_TYPE4.contains("C")) {
            Idetification_type4 = "Pan Card Number";
        } else if (IDENT_TYPE4.contains("E")) {
            Idetification_type4 = "Aadhar Card Nummber";
        } else if (IDENT_TYPE4.contains("B")) {
            Idetification_type4 = "Voter s Identity Card Number";
        } else {
            Idetification_type4 = IDENT_TYPE4;
        }

        if (!IDENT_TYPE4.equalsIgnoreCase("")) {
            strFormatedCKYCDetails += "<pre><b>" + Idetification_type4 + " : </b>" + IDENT_NUM4 + "</pre><br/>";
        }


        String Idetification_type5 = "";
        if (IDENT_TYPE5.contains("A")) {
            Idetification_type5 = "Passport Number";
        } else if (IDENT_TYPE5.contains("D")) {
            Idetification_type5 = "Driving License Number";
        } else if (IDENT_TYPE5.contains("C")) {
            Idetification_type5 = "Pan Card Number";
        } else if (IDENT_TYPE5.contains("E")) {
            Idetification_type5 = "Aadhar Card Nummber";
        } else if (IDENT_TYPE5.contains("B")) {
            Idetification_type5 = "Voter s Identity Card Number";
        } else {
            Idetification_type5 = IDENT_TYPE5;
        }
        if (!IDENT_TYPE5.equalsIgnoreCase("")) {
            strFormatedCKYCDetails += "<pre><b>" + Idetification_type5 + " : </b>" + IDENT_NUM5 + "</pre><br/>";
        }

        String Idetification_type6 = "";
        if (IDENT_TYPE6.contains("A")) {
            Idetification_type6 = "Passport Number";
        } else if (IDENT_TYPE6.contains("D")) {
            Idetification_type6 = "Driving License Number";
        } else if (IDENT_TYPE6.contains("C")) {
            Idetification_type6 = "Pan Card Number";
        } else if (IDENT_TYPE6.contains("E")) {
            Idetification_type6 = "Aadhar Card Nummber";
        } else if (IDENT_TYPE6.contains("B")) {
            Idetification_type6 = "Voter s Identity Card Number";
        } else {
            Idetification_type6 = IDENT_TYPE6;
        }
        if (!IDENT_TYPE6.equalsIgnoreCase("")) {
            strFormatedCKYCDetails += "<pre><b>" + Idetification_type6 + " : </b>" + IDENT_NUM6 + "</pre><br/>";
        }

        return strFormatedCKYCDetails;
        //get_personal_info_xml();
    }

    public void get_personal_info_xml() {
        StringBuilder str_personal_info = new StringBuilder();

        String[] strTitle = FULLNAME.split(" ");
        String strName = FULLNAME.replace(strTitle[0], "");

        String strFormattedDOB = "";
        if (!str_user_dob.equals("")) {
            String[] arrDate = str_user_dob.split("-");
            strFormattedDOB = arrDate[1] + "-" + arrDate[0] + "-" + arrDate[2];
        }

        String strGender = (GENDER.equals("M") || GENDER.equals("m")) ? "Male" :
                ((GENDER.equals("F") || GENDER.equals("f")) ? "Female" : "Transgender");

        String strAadhaar = IDENT_TYPE1.contains("E") ? IDENT_NUM1 : (IDENT_TYPE1.contains("E") ? IDENT_NUM2 :
                (IDENT_TYPE3.contains("E") ? IDENT_NUM3 : (IDENT_TYPE4.contains("E") ? IDENT_NUM4 :
                        (IDENT_TYPE5.contains("E") ? IDENT_NUM5 : (IDENT_TYPE6.contains("E") ? IDENT_NUM6 : "")))));

        String strIsCommAddSame = (PERM_CORRES_SAMEFLAG.equals("N") || PERM_CORRES_SAMEFLAG.equals("n")) ? "false" : "true";

        String[] strFatherTitle = FATHER_FULLNAME.split(" ");
        String strFatherName = FATHER_FULLNAME.replace(strFatherTitle[0], "");

        String strRrelation = FATHERSPOUSE_FLAG.equals("01") ? "Father" : "Husband";

        str_personal_info.append("<personal_info_title>" + strTitle[0].substring(0, 1).toUpperCase()
                + strTitle[0].substring(1).toLowerCase() + "</personal_info_title>");
        str_personal_info.append("<personal_info_full_name>").append(strName.toUpperCase().trim()).append("</personal_info_full_name>");
        str_personal_info.append("<personal_info_dob>" + strFormattedDOB + "</personal_info_dob>");
        str_personal_info.append("<personal_info_gender>").append(strGender).append("</personal_info_gender>");
        str_personal_info.append("<personal_info_nationality>Indian</personal_info_nationality>");
        str_personal_info.append("<personal_info_aadhaar_no>").append(strAadhaar).append("</personal_info_aadhaar_no>");
        str_personal_info.append("<personal_info_pan_no>").append(str_CKYC_PAN_no).append("</personal_info_pan_no>");
        str_personal_info.append("<personal_info_communication_address1>").append(CORRES_LINE1.toUpperCase().trim()).append("</personal_info_communication_address1>");
        str_personal_info.append("<personal_info_communication_address2>").append(CORRES_LINE2.toUpperCase().trim()).append("</personal_info_communication_address2>");
        str_personal_info.append("<personal_info_communication_address3>").append(CORRES_LINE3.toUpperCase().trim()).append("</personal_info_communication_address3>");
        str_personal_info.append("<personal_info_communication_address_pin>").append(CORRES_PIN).append("</personal_info_communication_address_pin>");

        str_personal_info.append("<personal_info_communication_add_same>").append(strIsCommAddSame).append("</personal_info_communication_add_same>");
        if (strIsCommAddSame.equals("true")) {
            str_personal_info.append("<personal_info_permanant_address1>").append(CORRES_LINE1.toUpperCase().trim()).append("</personal_info_permanant_address1>");
            str_personal_info.append("<personal_info_permanant_address2>").append(CORRES_LINE2.toUpperCase().trim()).append("</personal_info_permanant_address2>");
            str_personal_info.append("<personal_info_permanant_address3>").append(CORRES_LINE3.toUpperCase().trim()).append("</personal_info_permanant_address3>");
            str_personal_info.append("<personal_info_permanant_address_pin>").append(CORRES_PIN).append("</personal_info_permanant_address_pin>");
        } else {
            str_personal_info.append("<personal_info_permanant_address1>").append(PERM_LINE1.toUpperCase().trim()).append("</personal_info_permanant_address1>");
            str_personal_info.append("<personal_info_permanant_address2>").append(PERM_LINE2.toUpperCase().trim()).append("</personal_info_permanant_address2>");
            str_personal_info.append("<personal_info_permanant_address3>").append(PERM_LINE3.toUpperCase().trim()).append("</personal_info_permanant_address3>");
            str_personal_info.append("<personal_info_permanant_address_pin>").append(PERM_PIN).append("</personal_info_permanant_address_pin>");
        }

        str_personal_info.append("<personal_info_father_husband_name>").append(strFatherName.toUpperCase().trim()).append("</personal_info_father_husband_name>");
        str_personal_info.append("<personal_info_relation_with_applicant>" + strRrelation + "</personal_info_relation_with_applicant>");
        str_personal_info.append("<personal_info_maiden_name>").append(MAIDEN_FULLNAME).append("</personal_info_maiden_name>");
        str_personal_info.append("<personal_info_marital_status></personal_info_marital_status>");
        str_personal_info.append("<personal_info_caste_category></personal_info_caste_category>");
        str_personal_info.append("<personal_info_mobile_no>").append(MOB_NUM).append("</personal_info_mobile_no>");
        str_personal_info.append("<personal_info_residence_no>").append(RESI_STD_CODE + " " + RESI_TEL_NUM).append("</personal_info_residence_no>");
        str_personal_info.append("<personal_info_email_id>").append(EMAIL).append("</personal_info_email_id>");
        str_personal_info.append("<personal_info_educational_details_basic_qualification></personal_info_educational_details_basic_qualification>");
        str_personal_info.append("<personal_info_educational_details_passing_roll_no></personal_info_educational_details_passing_roll_no>");
        str_personal_info.append("<personal_info_educational_details_passing_university></personal_info_educational_details_passing_university>");
        str_personal_info.append("<personal_info_educational_details_passing_month_year></personal_info_educational_details_passing_month_year>");
        str_personal_info.append("<personal_info_educational_details_professional_qualification></personal_info_educational_details_professional_qualification>");
        str_personal_info.append("<personal_info_educational_details_professional_qualification_others></personal_info_educational_details_professional_qualification_others>");
        str_personal_info.append("<personal_info_ckyc_no>" + CKYC_NO + "</personal_info_ckyc_no>");
        /*str_personal_info.append("</personal_info>");*/

        String str_doc_upload = /*"<doc_upload_age_proof_type>CKYC</doc_upload_age_proof_type>"
                + "<doc_upload_age_proof_is_upload>true</doc_upload_age_proof_is_upload>"
                + */"<doc_upload_comm_address_type>CKYC</doc_upload_comm_address_type>"
                + "<doc_upload_comm_address_is_upload>true</doc_upload_comm_address_is_upload>";

        DatabaseHelper db = new DatabaseHelper(mContext);

        //3. update data against global row id
        ContentValues cv = new ContentValues();
        if (strEnrollType.equals("New")) {
            cv.put(db.AGENT_ON_BOARDING_AADHAAR_NO, strAadhaar);
            cv.put(db.AGENT_ON_BOARDING_AADHAAR_DETAILS, "");
            cv.put(db.AGENT_ON_BOARDING_PERSONAL_INFO, str_personal_info.toString());
            cv.put(db.AGENT_ON_BOARDING_DOCUMENTS_UPLOAD, str_doc_upload);

            cv.put(db.AGENT_ON_BOARDING_UPDATED_BY, mCommonMethods.GetUserCode(mContext));

            Calendar c = Calendar.getInstance();
            //save date in long
            cv.put(db.AGENT_ON_BOARDING_UPDATED_DATE, new Date(c.getTimeInMillis()).getTime() + "");
            cv.put(db.AGENT_ON_BOARDING_SYNCH_STATUS, "1");
            cv.put(db.AGENT_ON_BOARDING_ENROLLMENT_TYPE, strEnrollType);

            int i = db.update_agent_on_boarding_details(cv, db.AGENT_ON_BOARDING_ID + " =? ",
                    new String[]{Activity_AOB_Authentication.row_details + ""});

            Intent mIntent1 = new Intent(mContext, ActivityAOBPersonalInfo.class);
            mIntent1.putExtra("is_dashboard", false);
            mContext.startActivity(mIntent1);
        } else if (strEnrollType.equals(mCommonMethods.str_posp_ra_customer_type)) {
            //update status of POSP RA communication address
            cv.put(db.POSP_RA_PERSONAL_INFO, str_personal_info.toString());
            cv.put(db.POSP_RA_DOCUMENTS_UPLOAD, str_doc_upload);
            cv.put(db.POSP_RA_UPDATED_BY, mCommonMethods.GetUserCode(mContext));

            Calendar c = Calendar.getInstance();
            //save date in long
            cv.put(db.POSP_RA_UPDATED_DATE, new Date(c.getTimeInMillis()).getTime() + "");
            cv.put(db.POSP_RA_IN_APP_STATUS, "1");
            cv.put(db.POSP_RA_IN_APP_STATUS_REMARK, "CKYC Done");

            int i = db.update_POSP_RA_details(cv, db.POSP_RA_ID + " =? ",
                    new String[]{Activity_POSP_RA_Authentication.row_details + ""});

            Intent mIntent1 = new Intent(mContext, Activity_POSP_RA_PersonalInfo.class);
            mIntent1.putExtra("is_dashboard", false);
            mContext.startActivity(mIntent1);
        }
    }

    private class AsyncGetcKYCDownloadDetail extends AsyncTask<String, Void, String> {
        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setTitle(Html
                    .fromHtml("<font color='#00a1e3'><b>Please Wait...<b></font>"));
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            if (mCommonMethods.isNetworkConnected(mContext)) {

                try {
                /*DateFormat userDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                DateFormat dateFormatNeeded = new SimpleDateFormat("dd-MM-yyyy");
                Date date = userDateFormat.parse(DOB_PH);
                String NewDate = dateFormatNeeded.format(date);*/

                    SoapObject request = new SoapObject(Namespace, METHOD_NAME_GETCKYCDETAIL);

                    JSONObject json_obj = new JSONObject();
                    json_obj.put("MODE", "DOWNLOAD");
                    json_obj.put("CKYC_NO", CKYC_NO);
                    json_obj.put("AUTH_FACTOR_TYPE", "1");
                    json_obj.put("AUTH_FACTOR", str_user_dob); //DOB with format 'dd-MM-yyyy'
                    json_obj.put("Source", "SBIL");
                    json_obj.put("AuthKey", "$Sbilife@12345$");

                    request.addProperty("strInput", json_obj.toString());
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);
                    //allowAllSSL();
                    mCommonMethods.TLSv12Enable();
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    HttpTransportSE androidHttpTransportSE = new HttpTransportSE(ServiceURL.SERVICE_URL,
                            100000);
                    androidHttpTransportSE.call(Namespace + METHOD_NAME_GETCKYCDETAIL, envelope);

                    Object response = envelope.getResponse();
                    SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();
                    str_res_cKYC_download_dtls = sa.toString();
                    if (!str_res_cKYC_download_dtls.contains("PID_DATA")) {
                        str_res_cKYC_download_dtls = "0";
                    }

                } catch (Exception e) {
                    mProgressDialog.dismiss();
                    running = false;
                    str_res_cKYC_download_dtls = "0";
                }

                return str_res_cKYC_download_dtls;

            } else {
                running = false;
                return mCommonMethods.WEEK_INTERNET_MESSAGE;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (running) {

                if (!str_res_cKYC_download_dtls.equals("0")) {

                    try {
                        JSONObject json_Search = new JSONObject(str_res_cKYC_download_dtls);
                        String Output = json_Search.get("Output").toString();
                        String Error = json_Search.get("Error").toString();
                        if (Error.equals("")) {
                            if (str_res_cKYC_download_dtls.contains("IMAGE_DATA")) {
                            /*new AsyncUploadCKYCDoc(mContext, str_res_cKYC_download_dtls)
                                    .execute();*/
                                /*str_CKYC_increment = "1";
                                UploadCKYCDocuments();*/

                                mInterfaceCKYCProcessCompletion.onCKYCProcessComppletion(
                                        InterfaceCKYCProcessCompletion.CKYC_DOWNLOAD_DETAILS_PROCESS,
                                        true, str_res_cKYC_download_dtls);
                            }

                        } else {
                            //mCommonMethods.showToast(mContext, Error);
                            mInterfaceCKYCProcessCompletion.onCKYCProcessComppletion(
                                    InterfaceCKYCProcessCompletion.CKYC_DOWNLOAD_DETAILS_PROCESS,
                                    false, Error);
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        //mCommonMethods.showToast(mContext, "Error in KYC XML");
                        mInterfaceCKYCProcessCompletion.onCKYCProcessComppletion(InterfaceCKYCProcessCompletion.CKYC_DOWNLOAD_DETAILS_PROCESS,
                                false, "Error in CKYC XML");
                    }
                } else {
                    //mCommonMethods.showToast(mContext, "cKYC Data not Retrived");
                    mInterfaceCKYCProcessCompletion.onCKYCProcessComppletion(InterfaceCKYCProcessCompletion.CKYC_DOWNLOAD_DETAILS_PROCESS,
                            false, "cKYC Data not Retrived");
                }

            } else {
                //mCommonMethods.showToast(mContext, s);
                mInterfaceCKYCProcessCompletion.onCKYCProcessComppletion(InterfaceCKYCProcessCompletion.CKYC_DOWNLOAD_DETAILS_PROCESS,
                        false, s);
            }
        }
    }
}
