package sbilife.com.pointofsale_bancaagency;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.home.CarouselHomeActivity;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

@SuppressWarnings("deprecation")
public class ProfileActivity extends AppCompatActivity {

    private TextView txtpersonal, txt_professional;
    private TableRow tblStatus, tbledStatus, tblCBUCode, tbledCBUCode;

    private TableRow tblname, tbledname, tbladdress, tbledaddress, tblsecqa, tbledsecqa;
    private TableRow tbledsecans, tblmobno, tbledmobno, tbldob, tbleddob, tblemailid, tbledemailid;
    private TableRow tblpassword, tbledpassword, tblconfpassword, tbledconfpassword;

    protected ListView lv;

    private final Context context = this;
    private DatabaseHelper dbHelper;

    private DownloadFileAsyncAgentProfile taskAgentProfile;
    private  final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;

    private String code = "";
    private String emailid = "";
    private String mob = "";
    private String password = "";
    private String strAuthUserErrorCOde = "";

    private String strName = "";
    private String strLicNo = "";
    private String strLicIssueDT = "";
    private String strLicExpDT = "";
    private String strActivationDT = "";
    private String strBranchName = "";
    private String strRegion = "";
    private String strUMName = "";
    private String strStatus = "";
    private String strULIPCert = "";

    private String strAgent = "";

    private EditText edlicenceno, edlicenceissuedt, edlicenceexpdt, edactivationstdt, edbr, edregion, edumname, edstatus, edulipcert;

    private TableRow tblpro, tbllicno, tbledlicno, tbllicesudt, tbledlicesudt, tbllicexpdt, tbledlicexpdt, tblactstdt;
    private TableRow tbldtactstdt, tblbrnm, tbledbrnm, tblregion, tbledregion, tblum, tbledum, tblstatus, tbledstatus, tblulipcer, tbledulipcer;

    private  final String NAMESPACE = "http://tempuri.org/";
    private  final String URl = ServiceURL.SERVICE_URL;
    private  final String METHOD_NAME_AGENT_PROFILE = "getAgentProfileDtls";

    private String SOAP_ACTION_SH = "http://tempuri.org/saveSmartAdvisorServiceHits";
    private String METHOD_NAME_SH = "saveSmartAdvisorServiceHits";
    private String username = "";
    private CommonMethods mCommonMethods;
    private ServiceHits service;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.profile);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        dbHelper = new DatabaseHelper(this);
        mCommonMethods = new CommonMethods();

        taskAgentProfile = new DownloadFileAsyncAgentProfile();


        username = dbHelper.GetUserName();
        mCommonMethods.setApplicationToolbarMenu(this, "Profile");

        EditText edtitle = findViewById(R.id.usertitle);
        /*
		 * EditText edfname = (EditText) findViewById(R.id.userfirstname);
		 * EditText lname = (EditText) findViewById(R.id.userlastname);
		 */
        EditText address = findViewById(R.id.useraddress);
        EditText status = findViewById(R.id.userstatus);
        EditText cifno = findViewById(R.id.usercifno);
        EditText patename = findViewById(R.id.userpatename);
        EditText email = findViewById(R.id.email);
        EditText pass = findViewById(R.id.Userpassword);
        EditText confirmpass = findViewById(R.id.Userconfirmpassword);
        EditText edsecurityque = findViewById(R.id.edsecurityque);
        EditText mobileno = findViewById(R.id.mobileno);
        EditText dob = findViewById(R.id.dob);

        edlicenceno = findViewById(R.id.edlicenceno);
        edlicenceissuedt = findViewById(R.id.edlicenceissuedt);
        edlicenceexpdt = findViewById(R.id.edlicenceexpdt);
        edactivationstdt = findViewById(R.id.edactivationstdt);
        edbr = findViewById(R.id.edbr);
        edregion = findViewById(R.id.edregion);
        edumname = findViewById(R.id.edumname);
        edstatus = findViewById(R.id.edstatus);
        edulipcert = findViewById(R.id.edulipcert);

		/*TextView txtprofessional = (TextView) findViewById(R.id.txtprofessional);
		txtprofessional.setText(Html.fromHtml("<u>Professional</u>"));*/

        tblpro = findViewById(R.id.tblpro);
        tbllicno = findViewById(R.id.tbllicno);
        tbledlicno = findViewById(R.id.tbledlicno);
        tbllicesudt = findViewById(R.id.tbllicesudt);
        tbledlicesudt = findViewById(R.id.tbledlicesudt);
        tbllicexpdt = findViewById(R.id.tbllicexpdt);
        tbledlicexpdt = findViewById(R.id.tbledlicexpdt);
        tblactstdt = findViewById(R.id.tblactstdt);
        tbldtactstdt = findViewById(R.id.tbldtactstdt);
        tblbrnm = findViewById(R.id.tblbrnm);
        tbledbrnm = findViewById(R.id.tbledbrnm);
        tblregion = findViewById(R.id.tblregion);
        tbledregion = findViewById(R.id.tbledregion);
        tblum = findViewById(R.id.tblum);
        tbledum = findViewById(R.id.tbledum);
        tblstatus = findViewById(R.id.tblstatus);
        tbledstatus = findViewById(R.id.tbledstatus);
        tblulipcer = findViewById(R.id.tblulipcer);
        tbledulipcer = findViewById(R.id.tbledulipcer);

        txtpersonal = findViewById(R.id.txtpersonal);
        txt_professional = findViewById(R.id.txt_professional);

        tblStatus = findViewById(R.id.tblStatus);
        tbledStatus = findViewById(R.id.tbledStatus);
        tblCBUCode = findViewById(R.id.tblCBUCode);
        tbledCBUCode = findViewById(R.id.tbledCBUCode);


        tblname = findViewById(R.id.tblname);
        tbledname = findViewById(R.id.tbledname);
        tbladdress = findViewById(R.id.tbladdress);
        tbledaddress = findViewById(R.id.tbledaddress);
        tblsecqa = findViewById(R.id.tblsecqa);
        tbledsecqa = findViewById(R.id.tbledsecqa);
        tbledsecans = findViewById(R.id.tbledsecans);
        tblmobno = findViewById(R.id.tblmobno);
        tbledmobno = findViewById(R.id.tbledmobno);
        tbldob = findViewById(R.id.tbldob);
        tbleddob = findViewById(R.id.tbleddob);
        tblemailid = findViewById(R.id.tblemailid);
        tbledemailid = findViewById(R.id.tbledemailid);
        tblpassword = findViewById(R.id.tblpassword);
        tbledpassword = findViewById(R.id.tbledpassword);
        tblconfpassword = findViewById(R.id.tblconfpassword);
        tbledconfpassword = findViewById(R.id.tbledconfpassword);

        txtpersonal.setBackgroundResource(R.drawable.exp_selected);
        txt_professional.setBackgroundResource(R.drawable.exp_unselected);

        TextView txtUsertype = findViewById(R.id.txtUsertype);

        final String UserType = mCommonMethods.GetUserType(context);

        if (UserType.contentEquals("MAN") || UserType.contentEquals("BDM") || UserType.contentEquals("CIF")) {
            txtUsertype.setText("CIF Code/ BDM Code/ User Id");
            tbldob.setVisibility(View.GONE);
            tbleddob.setVisibility(View.GONE);
        } else {
            txtUsertype.setText("IA Code");
            tbldob.setVisibility(View.VISIBLE);
            tbleddob.setVisibility(View.VISIBLE);
        }

        tblpro.setVisibility(View.GONE);
        tbllicno.setVisibility(View.GONE);
        tbledlicno.setVisibility(View.GONE);
        tbllicesudt.setVisibility(View.GONE);
        tbledlicesudt.setVisibility(View.GONE);
        tbllicexpdt.setVisibility(View.GONE);
        tbledlicexpdt.setVisibility(View.GONE);
        tblactstdt.setVisibility(View.GONE);
        tbldtactstdt.setVisibility(View.GONE);
        tblbrnm.setVisibility(View.GONE);
        tbledbrnm.setVisibility(View.GONE);
        tblregion.setVisibility(View.GONE);
        tbledregion.setVisibility(View.GONE);
        tblum.setVisibility(View.GONE);
        tbledum.setVisibility(View.GONE);
        tblstatus.setVisibility(View.GONE);
        tbledstatus.setVisibility(View.GONE);
        tblulipcer.setVisibility(View.GONE);
        tbledulipcer.setVisibility(View.GONE);

        tblStatus.setVisibility(View.GONE);
        tbledStatus.setVisibility(View.GONE);
        tblCBUCode.setVisibility(View.GONE);
        tbledCBUCode.setVisibility(View.GONE);

        tblname.setVisibility(View.VISIBLE);
        tbledname.setVisibility(View.VISIBLE);
        tbladdress.setVisibility(View.VISIBLE);
        tbledaddress.setVisibility(View.VISIBLE);
        tblsecqa.setVisibility(View.GONE);
        tbledsecqa.setVisibility(View.GONE);
        tbledsecans.setVisibility(View.GONE);
        tblmobno.setVisibility(View.VISIBLE);
        tbledmobno.setVisibility(View.VISIBLE);
        //tbldob.setVisibility(View.VISIBLE);
        //tbleddob.setVisibility(View.VISIBLE);
        tblemailid.setVisibility(View.VISIBLE);
        tbledemailid.setVisibility(View.VISIBLE);
        tblpassword.setVisibility(View.GONE);
        tbledpassword.setVisibility(View.GONE);
        tblconfpassword.setVisibility(View.GONE);
        tbledconfpassword.setVisibility(View.GONE);


        txtpersonal.setOnClickListener(new OnClickListener() {

            //@Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                txtpersonal.setBackgroundResource(R.drawable.exp_selected);
                txt_professional.setBackgroundResource(R.drawable.exp_unselected);

                tblpro.setVisibility(View.GONE);
                tbllicno.setVisibility(View.GONE);
                tbledlicno.setVisibility(View.GONE);
                tbllicesudt.setVisibility(View.GONE);
                tbledlicesudt.setVisibility(View.GONE);
                tbllicexpdt.setVisibility(View.GONE);
                tbledlicexpdt.setVisibility(View.GONE);
                tblactstdt.setVisibility(View.GONE);
                tbldtactstdt.setVisibility(View.GONE);
                tblbrnm.setVisibility(View.GONE);
                tbledbrnm.setVisibility(View.GONE);
                tblregion.setVisibility(View.GONE);
                tbledregion.setVisibility(View.GONE);
                tblum.setVisibility(View.GONE);
                tbledum.setVisibility(View.GONE);
                tblstatus.setVisibility(View.GONE);
                tbledstatus.setVisibility(View.GONE);
                tblulipcer.setVisibility(View.GONE);
                tbledulipcer.setVisibility(View.GONE);

                tblStatus.setVisibility(View.GONE);
                tbledStatus.setVisibility(View.GONE);
                tblCBUCode.setVisibility(View.GONE);
                tbledCBUCode.setVisibility(View.GONE);

                tblname.setVisibility(View.VISIBLE);
                tbledname.setVisibility(View.VISIBLE);
                tbladdress.setVisibility(View.VISIBLE);
                tbledaddress.setVisibility(View.VISIBLE);
                tblsecqa.setVisibility(View.GONE);
                tbledsecqa.setVisibility(View.GONE);
                tbledsecans.setVisibility(View.GONE);
                tblmobno.setVisibility(View.VISIBLE);
                tbledmobno.setVisibility(View.VISIBLE);
                //tbldob.setVisibility(View.VISIBLE);
                //tbleddob.setVisibility(View.VISIBLE);
                tblemailid.setVisibility(View.VISIBLE);
                tbledemailid.setVisibility(View.VISIBLE);
                tblpassword.setVisibility(View.GONE);
                tbledpassword.setVisibility(View.GONE);
                tblconfpassword.setVisibility(View.GONE);
                tbledconfpassword.setVisibility(View.GONE);


                if (UserType.contentEquals("MAN") || UserType.contentEquals("BDM") || UserType.contentEquals("CIF")) {
                    tbldob.setVisibility(View.GONE);
                    tbleddob.setVisibility(View.GONE);
                } else {
                    tbldob.setVisibility(View.VISIBLE);
                    tbleddob.setVisibility(View.VISIBLE);
                }
            }
        });

        txt_professional.setOnClickListener(new OnClickListener() {

            //@Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                txtpersonal.setBackgroundResource(R.drawable.exp_unselected);
                txt_professional.setBackgroundResource(R.drawable.exp_selected);
				
				/*//tblpro.setVisibility(View.VISIBLE);
				tbllicno.setVisibility(View.VISIBLE);
				tbledlicno.setVisibility(View.VISIBLE);
				tbllicesudt.setVisibility(View.VISIBLE);
				tbledlicesudt.setVisibility(View.VISIBLE);
				tbllicexpdt.setVisibility(View.VISIBLE);
				tbledlicexpdt.setVisibility(View.VISIBLE);
				tblactstdt.setVisibility(View.VISIBLE);
				tbldtactstdt.setVisibility(View.VISIBLE);
				tblbrnm.setVisibility(View.VISIBLE);
				tbledbrnm.setVisibility(View.VISIBLE);
				tblregion.setVisibility(View.VISIBLE);
				tbledregion.setVisibility(View.VISIBLE);
				tblum.setVisibility(View.VISIBLE);
				tbledum.setVisibility(View.VISIBLE);
				//tblstatus.setVisibility(View.VISIBLE);
				//tbledstatus.setVisibility(View.VISIBLE);
				tblulipcer.setVisibility(View.VISIBLE);
				tbledulipcer.setVisibility(View.VISIBLE);*/

                tblStatus.setVisibility(View.VISIBLE);
                tbledStatus.setVisibility(View.VISIBLE);
                tblCBUCode.setVisibility(View.VISIBLE);
                tbledCBUCode.setVisibility(View.VISIBLE);

                tblname.setVisibility(View.GONE);
                tbledname.setVisibility(View.GONE);
                tbladdress.setVisibility(View.GONE);
                tbledaddress.setVisibility(View.GONE);
                tblsecqa.setVisibility(View.GONE);
                tbledsecqa.setVisibility(View.GONE);
                tbledsecans.setVisibility(View.GONE);
                tblmobno.setVisibility(View.GONE);
                tbledmobno.setVisibility(View.GONE);
                tbldob.setVisibility(View.GONE);
                tbleddob.setVisibility(View.GONE);
                tblemailid.setVisibility(View.GONE);
                tbledemailid.setVisibility(View.GONE);
                tblpassword.setVisibility(View.GONE);
                tbledpassword.setVisibility(View.GONE);
                tblconfpassword.setVisibility(View.GONE);
                tbledconfpassword.setVisibility(View.GONE);


                    String type = "";
                    try {
                        type = SimpleCrypto.decrypt("SBIL", dbHelper.GetUserType());
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    if (type.contentEquals("AGENT")) {

                        //tblpro.setVisibility(View.VISIBLE);
                        tbllicno.setVisibility(View.VISIBLE);
                        tbledlicno.setVisibility(View.VISIBLE);
                        tbllicesudt.setVisibility(View.VISIBLE);
                        tbledlicesudt.setVisibility(View.VISIBLE);
                        tbllicexpdt.setVisibility(View.VISIBLE);
                        tbledlicexpdt.setVisibility(View.VISIBLE);
                        tblactstdt.setVisibility(View.VISIBLE);
                        tbldtactstdt.setVisibility(View.VISIBLE);
                        tblbrnm.setVisibility(View.VISIBLE);
                        tbledbrnm.setVisibility(View.VISIBLE);
                        tblregion.setVisibility(View.VISIBLE);
                        tbledregion.setVisibility(View.VISIBLE);
                        tblum.setVisibility(View.VISIBLE);
                        tbledum.setVisibility(View.VISIBLE);
                        //tblstatus.setVisibility(View.VISIBLE);
                        //tbledstatus.setVisibility(View.VISIBLE);
                        tblulipcer.setVisibility(View.VISIBLE);
                        tbledulipcer.setVisibility(View.VISIBLE);


                    } else {

                        tblpro.setVisibility(View.GONE);
                        tbllicno.setVisibility(View.GONE);
                        tbledlicno.setVisibility(View.GONE);
                        tbllicesudt.setVisibility(View.GONE);
                        tbledlicesudt.setVisibility(View.GONE);
                        tbllicexpdt.setVisibility(View.GONE);
                        tbledlicexpdt.setVisibility(View.GONE);
                        tblactstdt.setVisibility(View.GONE);
                        tbldtactstdt.setVisibility(View.GONE);
                        tblbrnm.setVisibility(View.GONE);
                        tbledbrnm.setVisibility(View.GONE);
                        tblregion.setVisibility(View.GONE);
                        tbledregion.setVisibility(View.GONE);
                        tblum.setVisibility(View.GONE);
                        tbledum.setVisibility(View.GONE);
                        tblstatus.setVisibility(View.GONE);
                        tbledstatus.setVisibility(View.GONE);
                        tblulipcer.setVisibility(View.GONE);
                        tbledulipcer.setVisibility(View.GONE);

                    }
            }
        });

        ArrayList<String> lstevent = new ArrayList<String>();

        lstevent.clear();

		/*
		 * it will set all detail of registered user in form
		 */
        Cursor curserLogin = dbHelper.GetProfile();
        if (curserLogin.getCount() > 0) {
            curserLogin.moveToFirst();
            for (int ii = 0; ii < curserLogin.getCount(); ii++) {
                lstevent.add(curserLogin.getString(curserLogin.getColumnIndex("LoginTitle")));
                lstevent.add(curserLogin.getString(curserLogin.getColumnIndex("LoginFirstName")));
                lstevent.add(curserLogin.getString(curserLogin.getColumnIndex("LoginLastName")));
                lstevent.add(curserLogin.getString(curserLogin.getColumnIndex("LoginAddress")));
                lstevent.add(curserLogin.getString(curserLogin.getColumnIndex("LoginStatus")));
                lstevent.add(curserLogin.getString(curserLogin.getColumnIndex("LoginCIFNo")));
                lstevent.add(curserLogin.getString(curserLogin.getColumnIndex("LoginPateName")));
                lstevent.add(curserLogin.getString(curserLogin.getColumnIndex("LoginEmail")));
                lstevent.add(curserLogin.getString(curserLogin.getColumnIndex("LoginPassword")));
                lstevent.add(curserLogin.getString(curserLogin.getColumnIndex("LoginConfirmPassword")));
                lstevent.add(curserLogin.getString(curserLogin.getColumnIndex("LoginQuestion")));
                lstevent.add(curserLogin.getString(curserLogin.getColumnIndex("LoginMobileNo")));
                lstevent.add(curserLogin.getString(curserLogin.getColumnIndex("LoginDOB")));
                curserLogin.moveToNext();
            }
        }

        try {
            String name = SimpleCrypto.decrypt("SBIL", lstevent.get(0))
                    + " "
                    + SimpleCrypto.decrypt("SBIL", lstevent.get(1))
                    + " "
                    + SimpleCrypto.decrypt("SBIL", lstevent.get(2));

            edtitle.setText(name);

				/*
				 * edtitle.setText(SimpleCrypto.decrypt("SBIL",lstevent.get(0).
				 * toString()));
				 * edfname.setText(SimpleCrypto.decrypt("SBIL",lstevent
				 * .get(1).toString()));
				 * lname.setText(SimpleCrypto.decrypt("SBIL"
				 * ,lstevent.get(2).toString()));
				 */
            String addressString = lstevent.get(3) == null ? "" : lstevent.get(3);

            if (addressString.length() != 0) {
                address.setText(addressString);
            }

            String statusString = lstevent.get(4) == null ? "" : lstevent.get(4);
            if (statusString.length() != 0) {
                status.setText(statusString);
            }

            String cifnoString = lstevent.get(5) == null ? "" : lstevent.get(5);
            if (cifnoString.length() != 0) {
                cifnoString = SimpleCrypto.decrypt("SBIL", cifnoString);
                cifno.setText(cifnoString);
                code = cifnoString;
            }


            String pateNameString = lstevent.get(6) == null ? "" : lstevent.get(6);
            String emailString = lstevent.get(7) == null ? "" : lstevent.get(7);

            if (pateNameString.length() != 0) {
                patename.setText(SimpleCrypto.decrypt("SBIL", pateNameString));
            }
            if (emailString.length() != 0) {
                emailString = SimpleCrypto.decrypt("SBIL", emailString);
                emailid = emailString;
            }
            email.setText(emailString);

            String passwordString = lstevent.get(8) == null ? "" : lstevent.get(8);

            if (passwordString.length() != 0) {
                pass.setText(SimpleCrypto.decrypt("SBIL", passwordString));
                password = passwordString;
            }

            String confirmpassString = lstevent.get(9) == null ? "" : lstevent.get(9);
            if (confirmpassString.length() != 0) {
                confirmpassString = SimpleCrypto.decrypt("SBIL", confirmpassString);
            }
            confirmpass.setText(confirmpassString);

            String edSecurityQueString = lstevent.get(10) == null ? "" : lstevent.get(10);
            if (edSecurityQueString.length() != 0) {
                edSecurityQueString = SimpleCrypto.decrypt("SBIL", edSecurityQueString);
            }
            edsecurityque.setText(edSecurityQueString);

            String mobileNoString = lstevent.get(11) == null ? "" : lstevent.get(11);
            if (mobileNoString.length() != 0) {
                mobileNoString = SimpleCrypto.decrypt("SBIL", mobileNoString);
            }
            mobileno.setText(mobileNoString);
            mob = mobileNoString;

            String dobString = lstevent.get(12) == null ? "" :
                    lstevent.get(12);
            if (dobString.length() != 0) {
                dobString = SimpleCrypto.decrypt("SBIL", dobString);
            }

            dob.setText(dobString);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        String type = "";
        try {
            type = SimpleCrypto.decrypt("SBIL", dbHelper.GetUserType());
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        if (type.contentEquals("AGENT")) {
            strAgent = "";
            int UserId = dbHelper.GetAgentProfileId();
            if (UserId == 0) {
                //startdownloadAgentProfile();
                service_hits();
            } else {

                ArrayList<String> lstdata = new ArrayList<String>();
                lstdata.clear();

                Cursor c = dbHelper.GetAgentProfile();
                if (c.getCount() > 0) {
                    c.moveToFirst();
                    for (int ii = 0; ii < c.getCount(); ii++) {
                        lstdata.add(c.getString(c
                                .getColumnIndex("AgentName")));
                        lstdata.add(c.getString(c
                                .getColumnIndex("AgentLicenceNo")));
                        lstdata.add(c.getString(c
                                .getColumnIndex("AgentLicenceIssuDate")));
                        lstdata.add(c.getString(c
                                .getColumnIndex("AgentLicenceExpiryDate")));
                        lstdata.add(c.getString(c
                                .getColumnIndex("AgentActivationStartDate")));
                        lstdata.add(c.getString(c
                                .getColumnIndex("AgentProfileBranchName")));
                        lstdata.add(c.getString(c
                                .getColumnIndex("AgentProfileRegion")));
                        lstdata.add(c.getString(c
                                .getColumnIndex("AgentProfileUMName")));
                        lstdata.add(c.getString(c
                                .getColumnIndex("AgentProfileStatus")));
                        lstdata.add(c.getString(c
                                .getColumnIndex("AgentProfileULIPCertified")));
                        c.moveToNext();
                    }
                }

                try {

                    edlicenceno.setText(SimpleCrypto.decrypt("SBIL",
                            lstdata.get(1)));
                    edlicenceissuedt.setText(SimpleCrypto.decrypt("SBIL",
                            lstdata.get(2)));
                    edlicenceexpdt.setText(SimpleCrypto.decrypt("SBIL",
                            lstdata.get(3)));
                    edactivationstdt.setText(SimpleCrypto.decrypt("SBIL",
                            lstdata.get(4)));
                    edbr.setText(SimpleCrypto.decrypt("SBIL",
                            lstdata.get(5)));
                    edregion.setText(SimpleCrypto.decrypt("SBIL", lstdata
                            .get(6)));
                    edumname.setText(SimpleCrypto.decrypt("SBIL", lstdata
                            .get(7)));
                    edstatus.setText(SimpleCrypto.decrypt("SBIL", lstdata
                            .get(8)));
                    edulipcert.setText(SimpleCrypto.decrypt("SBIL",
                            lstdata.get(9)));

                } catch (Exception e) {
                    // TODO Auto-generated catch block

                }
            }
        }

    }

    public void btnAdd_Click(View v) {
        finish();
    }

    private void startdownloadAgentProfile() {
        taskAgentProfile = new DownloadFileAsyncAgentProfile();
        taskAgentProfile.execute("demo");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (taskAgentProfile != null) {
                taskAgentProfile.cancel(true);
            }
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }
            if (service != null) {
                service.cancel(true);
            }
            finish();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
                String Message = "Loading. Please wait...";
                mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setCancelable(true);

                mProgressDialog.setButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                if (taskAgentProfile != null) {
                                    taskAgentProfile.cancel(true);
                                }
                                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                    mProgressDialog.dismiss();
                                }
                            }
                        });

                mProgressDialog.setMax(100);
                mProgressDialog.show();
                return mProgressDialog;

            default:
                return null;
        }
    }

    class DownloadFileAsyncAgentProfile extends
            AsyncTask<String, String, String> {

        private volatile boolean running = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME_AGENT_PROFILE);
                request.addProperty("strAgentNo", code);
                request.addProperty("strEmailId", emailid);
                request.addProperty("strMobileNo", mob);
                request.addProperty("strAuthKey", password.trim());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                // 	allowAllSSL();
                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URl);
                try {
                    String SOAP_ACTION_AGENT_PROFILE = "http://tempuri.org/getAgentProfileDtls";
                    androidHttpTranport.call(SOAP_ACTION_AGENT_PROFILE,
                            envelope);

                    SoapPrimitive sa = null;
                    try {
                        sa = (SoapPrimitive) envelope.getResponse();

                        String inputpolicylist = sa.toString();

                        ParseXML prsObj = new ParseXML();

                        /*inputpolicylist = prsObj.parseXmlTag(inputpolicylist,
                                "CIFPolicyList");*/
                        inputpolicylist = prsObj.parseXmlTag(inputpolicylist,
                                "AgentProfile");
                        inputpolicylist = prsObj.parseXmlTag(
                                inputpolicylist, "ScreenData");
                        strAuthUserErrorCOde = inputpolicylist;

                        if (strAuthUserErrorCOde == null) {
                            inputpolicylist = sa.toString();
                            inputpolicylist = prsObj.parseXmlTag(
                                    inputpolicylist, "AgentProfile");

                            /*inputpolicylist = prsObj.parseXmlTag(inputpolicylist,
                                    "CIFPolicyList");*/
                            inputpolicylist = prsObj.parseXmlTag(
                                    inputpolicylist, "Table");

                            strName = prsObj.parseXmlTag(
                                    inputpolicylist, "IANAME");
                            strLicNo = prsObj.parseXmlTag(
                                    inputpolicylist, "LICENSE_NUM");
                            strLicIssueDT = prsObj.parseXmlTag(
                                    inputpolicylist, "LICENSEISSUEDATE");
                            strLicExpDT = prsObj.parseXmlTag(
                                    inputpolicylist, "LICENSEEXPDATE");//LICENSEEXPDATE
                            strActivationDT = prsObj.parseXmlTag(
                                    inputpolicylist, "ACTIVATION_START_DATE");
                            strBranchName = prsObj.parseXmlTag(
                                    inputpolicylist, "BRANCHNAME");
							
							/*strBranchCode = prsObj.parseXmlTag(
									inputpolicylist, "BRANCHCODE");*/

                            strRegion = prsObj.parseXmlTag(
                                    inputpolicylist, "REGION");
                            strUMName = prsObj.parseXmlTag(
                                    inputpolicylist, "UMNAME");
                            strStatus = prsObj.parseXmlTag(
                                    inputpolicylist, "STATUS");
                            //ULIPCERTIFIED
                            strULIPCert = prsObj.parseXmlTag(
                                    inputpolicylist, "EMPLOYEEID");
                            String strDOB = prsObj.parseXmlTag(
                                    inputpolicylist, "DOB");
                            strULIPCert = prsObj.parseXmlTag(
                                    inputpolicylist, "ADDR_LINE1");
                            strULIPCert = prsObj.parseXmlTag(
                                    inputpolicylist, "ADDR_LINE2");
                            strULIPCert = prsObj.parseXmlTag(
                                    inputpolicylist, "ULIPCERTIFIED");

                            if (strLicIssueDT == null) {
                                strLicIssueDT = "";
                            } else {

                                strLicIssueDT = strLicIssueDT.split("T")[0];

                                Date dt1 = null;
                                SimpleDateFormat df = new SimpleDateFormat(
                                        "yyyy-MM-dd");
                                SimpleDateFormat df1 = new SimpleDateFormat(
                                        "dd-MMMM-yyyy");
                                try {
                                    dt1 = df.parse(strLicIssueDT);
                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                strLicIssueDT = df1.format(dt1);

                            }

                            if (strLicExpDT == null) {
                                strLicExpDT = "";
                            } else {

                                strLicExpDT = strLicExpDT.split("T")[0];

                                Date dt1 = null;
                                SimpleDateFormat df = new SimpleDateFormat(
                                        "yyyy-MM-dd");
                                SimpleDateFormat df1 = new SimpleDateFormat(
                                        "dd-MMMM-yyyy");
                                try {
                                    dt1 = df.parse(strLicExpDT);
                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                strLicExpDT = df1.format(dt1);

                            }

                            if (strActivationDT == null) {
                                strActivationDT = "";
                            } else {

                                strActivationDT = strActivationDT.split("T")[0];

                                Date dt1 = null;
                                SimpleDateFormat df = new SimpleDateFormat(
                                        "yyyy-MM-dd");
                                SimpleDateFormat df1 = new SimpleDateFormat(
                                        "dd-MMMM-yyyy");
                                try {
                                    dt1 = df.parse(strActivationDT);
                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                strActivationDT = df1.format(dt1);

                            }

                            if (strDOB == null) {
                                strDOB = "";
                            } else {

                                strDOB = strDOB.split("T")[0];

                                Date dt1 = null;
                                SimpleDateFormat df = new SimpleDateFormat(
                                        "yyyy-MM-dd");
                                SimpleDateFormat df1 = new SimpleDateFormat(
                                        "dd-MMMM-yyyy");
                                try {
                                    dt1 = df.parse(strDOB);
                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                strDOB = df1.format(dt1);

                            }

                        } else {
                            // txterrordesc.setText("No Data");
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
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            }
            if (running) {
                if (strAuthUserErrorCOde == null) {
                    boolean ok = true;
                    try {

                        if (strULIPCert.contentEquals("TRUE")) {
                            strULIPCert = "YES";
                        } else {
                            strULIPCert = "NO";
                        }

                        if (!strAgent.contentEquals("Agent")) {

                            clsAgentProfile log = new clsAgentProfile(
                                    SimpleCrypto.encrypt("SBIL",
                                            strName == null ? "" : strName),
                                    SimpleCrypto.encrypt("SBIL",
                                            strLicNo == null ? "" : strLicNo),
                                    SimpleCrypto.encrypt("SBIL",
                                            strLicIssueDT == null ? ""
                                                    : strLicIssueDT),
                                    SimpleCrypto.encrypt("SBIL",
                                            strLicExpDT == null ? "" : strLicExpDT),
                                    SimpleCrypto.encrypt("SBIL",
                                            strActivationDT == null ? ""
                                                    : strActivationDT),
                                    SimpleCrypto.encrypt("SBIL",
                                            strBranchName == null ? ""
                                                    : strBranchName), SimpleCrypto
                                    .encrypt("SBIL", strRegion == null ? ""
                                            : strRegion), SimpleCrypto
                                    .encrypt("SBIL", strUMName == null ? ""
                                            : strUMName), SimpleCrypto
                                    .encrypt("SBIL", strStatus == null ? ""
                                            : strStatus), SimpleCrypto
                                    .encrypt("SBIL",
                                            strULIPCert == null ? ""
                                                    : strULIPCert));

                            dbHelper.AddAgentProfile(log);

                            ArrayList<String> lstevent = new ArrayList<String>();
                            lstevent.clear();

                            Cursor c = dbHelper.GetAgentProfile();
                            if (c.getCount() > 0) {
                                c.moveToFirst();
                                for (int ii = 0; ii < c.getCount(); ii++) {
                                    lstevent.add(c.getString(c
                                            .getColumnIndex("AgentName")));
                                    lstevent.add(c.getString(c
                                            .getColumnIndex("AgentLicenceNo")));
                                    lstevent.add(c.getString(c
                                            .getColumnIndex("AgentLicenceIssuDate")));
                                    lstevent.add(c.getString(c
                                            .getColumnIndex("AgentLicenceExpiryDate")));
                                    lstevent.add(c.getString(c
                                            .getColumnIndex("AgentActivationStartDate")));
                                    lstevent.add(c.getString(c
                                            .getColumnIndex("AgentProfileBranchName")));
                                    lstevent.add(c.getString(c
                                            .getColumnIndex("AgentProfileRegion")));
                                    lstevent.add(c.getString(c
                                            .getColumnIndex("AgentProfileUMName")));
                                    lstevent.add(c.getString(c
                                            .getColumnIndex("AgentProfileStatus")));
                                    lstevent.add(c.getString(c
                                            .getColumnIndex("AgentProfileULIPCertified")));
                                    c.moveToNext();
                                }
                            }

                            try {

                                edlicenceno.setText(SimpleCrypto.decrypt("SBIL",
                                        lstevent.get(1)));
                                edlicenceissuedt.setText(SimpleCrypto.decrypt(
                                        "SBIL", lstevent.get(2)));
                                edlicenceexpdt.setText(SimpleCrypto.decrypt("SBIL",
                                        lstevent.get(3)));
                                edactivationstdt.setText(SimpleCrypto.decrypt(
                                        "SBIL", lstevent.get(4)));
                                edbr.setText(SimpleCrypto.decrypt("SBIL", lstevent
                                        .get(5)));
                                edregion.setText(SimpleCrypto.decrypt("SBIL",
                                        lstevent.get(6)));
                                edumname.setText(SimpleCrypto.decrypt("SBIL",
                                        lstevent.get(7)));
                                edstatus.setText(SimpleCrypto.decrypt("SBIL",
                                        lstevent.get(8)));
                                edulipcert.setText(SimpleCrypto.decrypt("SBIL",
                                        lstevent.get(9)));

                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        } else {
                            edlicenceno.setText(strLicNo == null ? "" : strLicNo);
                            edlicenceissuedt.setText(strLicIssueDT == null ? "" : strLicIssueDT);
                            edlicenceexpdt.setText(strLicExpDT == null ? "" : strLicExpDT);
                            edactivationstdt.setText(strActivationDT == null ? "" : strActivationDT);
                            edbr.setText(strBranchName == null ? "" : strBranchName);
                            edregion.setText(strRegion == null ? "" : strRegion);
                            edumname.setText(strUMName == null ? "" : strUMName);
                            edstatus.setText(strStatus == null ? "" : strStatus);
                            edulipcert.setText(strULIPCert == null ? "" : strULIPCert);
                        }

                    } catch (Exception ex) {
                        ok = false;
                    } finally {
                        if (ok) {
                            okAlert();
                        }
                    }
                } else {
                    VarifyUser();
                }
            } else {

                WeakReference<ProfileActivity> mainActivityWeakRef = new WeakReference<ProfileActivity>(ProfileActivity.this);

                if (mainActivityWeakRef.get() != null && !mainActivityWeakRef.get().isFinishing()) {
                    syncerror();
                }
            }
        }
    }

    private void service_hits() {

        mProgressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
        String Message = "Loading...";
        mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);

        service = new ServiceHits(context, mProgressDialog, NAMESPACE, URl, SOAP_ACTION_SH, METHOD_NAME_SH);
        service.execute();
    }


    class ServiceHits extends AsyncTask<String, Void, String> {
        Context mContext;
        ProgressDialog progressDialog = null;
        String NAMESPACE = "";
        String URL = "";
        String SOAP_ACTION = "";
        String METHOD_NAME = "";

        ServiceHits(Context mContext, ProgressDialog progressDialog, String NAMESPACE, String URL, String SOAP_ACTION, String METHOD_NAME) {
            // TODO Auto-generated constructor stub

            this.NAMESPACE = NAMESPACE;
            this.URL = URL;
            this.SOAP_ACTION = SOAP_ACTION;
            this.METHOD_NAME = METHOD_NAME;
            this.mContext = mContext;
            this.progressDialog = progressDialog;
        }

        @Override
        protected String doInBackground(String... param) {

            if (mCommonMethods.isNetworkConnected(context)) {

                try {

                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                    request.addProperty("serviceName", METHOD_NAME_AGENT_PROFILE);
                    request.addProperty("strProdCode", "");
                    request.addProperty("serviceInput", "");
                    request.addProperty("serviceReqUserId", code);
                    request.addProperty("strEmailId", emailid);
                    request.addProperty("strMobileNo", mob);
                    request.addProperty("strAuthKey", password.trim());


                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    //Enable this envelope if service is written in dot net
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

                    // 	allowAllSSL();
                    mCommonMethods.TLSv12Enable();

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    androidHttpTransport.call(SOAP_ACTION, envelope);
                    SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

                    String result = response.toString();

                    if (result.contains("1")) {
                        return "Success";
                    } else {
                        return "Failure";
                    }

                } catch (Exception e) {
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

            } catch (Exception e) {
                e.getMessage();
            }

            taskAgentProfile = new DownloadFileAsyncAgentProfile();


            if (result.equals("Success")) {
                startdownloadAgentProfile();

            } else {
                startdownloadAgentProfile();
            }
        }
    }


    private void okAlert() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("Agent Profile Successfully Downloaded..");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

	/*
	 * if user is not valid then this alert shows
	 */

    private void VarifyUser() {

        if (!isFinishing()) {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.loading_window);
            TextView text = dialog.findViewById(R.id.txtalertheader);
            text.setText("You are not Authorised User..!");
            Button dialogButton = dialog.findViewById(R.id.btnalert);
            dialogButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }

    private void syncerror() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("Server Not Responding,Try again..");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }



   /* @Override
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
    }*/

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ProfileActivity.this, CarouselHomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    @Override
    protected void onDestroy() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (taskAgentProfile != null) {
            taskAgentProfile.cancel(true);
        }

        if (service != null) {
            service.cancel(true);
        }

        super.onDestroy();
    }


}