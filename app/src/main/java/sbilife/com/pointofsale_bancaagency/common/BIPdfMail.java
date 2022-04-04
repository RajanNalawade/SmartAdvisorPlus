package sbilife.com.pointofsale_bancaagency.common;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.NA_CBI_bean;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.needanalysis.NeedAnalysisActivity;
import sbilife.com.pointofsale_bancaagency.needanalysis.OthersProductListActivity;
import sbilife.com.pointofsale_bancaagency.new_bussiness.NewBusinessHomeGroupingActivity;

public class BIPdfMail {

    private Context context;
    private String UIN_NO = "";
    public void MailPDF(String email, NA_CBI_bean na_cbi_bean, String proposer_name, File mypath,
                        File Graphical_NACBI_File, Context context, String QuatationNumber,String UIN_NO) {

        try {
            this.context = context;
            Calendar present_date = Calendar.getInstance();
            int mDay = present_date.get(Calendar.DAY_OF_MONTH);
            int mMonth = present_date.get(Calendar.MONTH);
            int mYear = present_date.get(Calendar.YEAR);
            File graphical_NACBI_File = Graphical_NACBI_File;
            File mypath1 = mypath;
            this.UIN_NO = UIN_NO;
            String CurrentDate = mDay + "-" + (mMonth + 1) + "-" + mYear;

            String proposerEmailId = email;


            DatabaseHelper db = new DatabaseHelper(context);
            ArrayList<String> lstevent = new ArrayList<>();
            lstevent.clear();
            Cursor c = db.GetProfile();
            if (c.getCount() > 0) {
                c.moveToFirst();
                for (int ii = 0; ii < c.getCount(); ii++) {
                    lstevent.add(c.getString(c.getColumnIndex("LoginTitle")));
                    lstevent.add(c.getString(c.getColumnIndex("LoginFirstName")));
                    lstevent.add(c.getString(c.getColumnIndex("LoginLastName")));
                    lstevent.add(c.getString(c.getColumnIndex("LoginAddress")));
                    lstevent.add(c.getString(c.getColumnIndex("LoginStatus")));
                    lstevent.add(c.getString(c.getColumnIndex("LoginCIFNo")));
                    lstevent.add(c.getString(c.getColumnIndex("LoginPateName")));
                    lstevent.add(c.getString(c.getColumnIndex("LoginEmail")));
                    lstevent.add(c.getString(c.getColumnIndex("LoginPassword")));
                    lstevent.add(c.getString(c.getColumnIndex("LoginConfirmPassword")));
                    lstevent.add(c.getString(c.getColumnIndex("LoginQuestion")));
                    lstevent.add(c.getString(c.getColumnIndex("LoginMobileNo")));
                    lstevent.add(c.getString(c.getColumnIndex("LoginDOB")));
                    c.moveToNext();
                }
            }

            String strProposerEmailId = proposerEmailId;
            String strPlanName = na_cbi_bean.getPlanName();
            String strAgentName = SimpleCrypto.decrypt("SBIL", lstevent.get(0)) + ". " + SimpleCrypto.decrypt("SBIL", lstevent.get(1)) + " " + SimpleCrypto.decrypt("SBIL", lstevent.get(2));
            String strAgentCode = SimpleCrypto.decrypt("SBIL", lstevent.get(5));
            String strAgentMobileNo = SimpleCrypto.decrypt("SBIL", lstevent.get(11));
            String strLicense_no = "????";

            String emailBody;

            if (db.GetUserType().equals("AGENT")) {

                emailBody = "Dear "
                        + proposer_name
                        + ","
                        + "\n"
                        + "\n"
                        + "We thank you for your interest in the SBI Life - "
                        + Html.fromHtml("<b>" + strPlanName + "</b>")
                        + " plan."
                        + "A e Quote has been generated as per the information provided by you."
                        + "\n"
                        + "Quotation reference number is "
                        + QuatationNumber
                        + "."
                        + "Dated "
                        + CurrentDate
                        + "\n"
                        + "Based on the inputs submitted by you, please find attached a Benefit Illustrator - "
                        + "\n"
                        + "Your  Sales Representative Details"
                        + "\n"
                        + "Name: "
                        + Html.fromHtml("<b>" + strAgentName + "</b>")
                        + "\n"
                        + "License No: "
                        + strLicense_no
                        + "\n"
                        + "Sales Representative Code: "
                        + Html.fromHtml("<b>" + strAgentCode + "</b>")
                        + "\n"
                        + "Mobile No: "
                        + Html.fromHtml("<b>" + strAgentMobileNo + "</b>")
                        + "\n"
                        + "\n"
                        + "You are requested to get in touch with your Insurance Advisor to complete the policy purchase at the earliest."
                        + "\n"
                        + "Please note that this Quotation No. is valid for a period of 30 days the date of generation."
                        + "\n"
                        + "\n"
                        + "For any assistance:"
                        + "\n"
                        + "Toll Free No.1800 267 9090 (Between 9:00 am to 9:00 pm)"
                        + "\n"
                        + "Email :info@sbilife.co.in"
                        + "\n"
                        + "\nRegards,\n"
                        + "SBI LIFE INSURANCE COMPANY LTD.\n"
                        + "Registered & Corporate Office: SBI Life Insurance Co. Ltd,\n"
                        + "Natraj, M.V. Road & Western Express Highway Junction, Andheri (East),\n"
                        + "Mumbai - 400 069.IRDAI Registration no. 111.\n"
                        + "website: www.sbilife.co.in | Email: info@sbilife.co.in | CIN: L99999MH2000PLC129113 | Toll Free: 1800 267 9090 (Between 9:00 AM & 9:00 PM)\n"
                        + "1) Most Trusted Private Life Insurance Brand 2013' thrice in a row, by ET Brand Equity"
                        + "\n"
                        + "2) SBI Life wins at Global Performance Excellence Award 2013"
                        + "\n"
                        + "3) Claim Settlement Ratio of 94.41%, as per SBI Life Annual Report for FY 2012 - 13"
                        + "\n"
                        + "4) 'The Most Admired Life Insurance Company in the Private Sector' by ABP BFSI Awards 2014"
                        + "\n"
                        + "\n"
                        + "Document Checklist: Keep the following self attested scanned copy ready"
                        + "\n"
                        + "Identity Proof : PAN Card, Driving Licence, Aadhar Card, Passport"
                        + "\n"
                        + "Age Proof (any one) : PAN Card, Birth Certificate, School / College Certificate, Passport, Driving License, Others"
                        + "\n"
                        + "Address Proof (any one) : Bank Account Statement, Electricity Bill, Letter from Recognized Public Authority, Ration Card, Telephone Bill"
                        + "\n"
                        + "Income Proof (any one) : Employer Certificate, Income Tax Returns, Assessment Order, Others"
                        + "\n"
                        + "Please Keep Your Internet Banking / Credit Card / Debit Card details for making the Premium Payment."
                        /*+ "\n"
						+ "\n"
						+ "Insurance is the subject matter of solicitation."*/
                        + "\n" + "\n"
                        + "This is a system generated mail. Please do not reply to this message.";
            } else {

                String strHeader = "Your Certified Insurance Facilitator (CIF) details are:";
                String Hname = "Name:";
                String Hcode = "CIF Code:";
                String Hmobile = "Mobile No:";
                String strCall = "Call us on our ";
                String strEmail = "Email us at ";
                String strDOC_Check_List = "Document Checklist:";
                String strIdenty = "Identity Proof (any one) : ";
                String strAge = "Age Proof (any one) : ";
                String strAddress = "Address Proof (any one) : ";
                String strIncome = "Income Proof (any one) : ";
                String strInternet = "Keep Your Internet Banking / Credit Card / Debit Card /Bank details ";
                String strSystem = "This is a system generated mail. Please do not reply to this message.";
                String Disclaimer = "“In this policy, the investment risk in the investment portfolio is borne by the policyholder”"
                        + "\n"
                        + "\n"
                        + "The Linked Insurance products do not offer any liquidity during the first five years of the contract. The policyholders will not be able to surrender/ withdraw the monies invested in Linked Insurance Products completely or partially till the end of fifth year."
                        + "\n"
                        + "\n"
                        + "SBI Life Insurance Co. Ltd. is only the name of the insurance company and SBI Life -"
                        + strPlanName
                        + " is only the name of the unit linked life insurance contract and does not in any way indicate the quality of the contract, its future prospects or returns.";
                emailBody = "Dear "
                        + proposer_name
                        + ","
                        + "\n"
                        + "\n"
                        + "We thank you for your interest in the SBI Life - "
                        + Html.fromHtml("<b>" + strPlanName + "</b>")
                        + "."
                        + "A Quote has been generated as per the information provided by you."
                        + "\n"
                        + "Please note that your Quotation reference number is "
                        + QuatationNumber
                        + ","
                        + "dated "
                        + Html.fromHtml("<b>" + CurrentDate + "</b>")
                        + "."
                        + "\n"
                        + "Based upon the inputs submitted by you, please find attached a Benefit Illustrator for your reference. "
                        + "\n"
                        + Html.fromHtml("<b><u>" + strHeader + "<u></b>")
                        + "\n"
                        + Html.fromHtml("<b>" + Hname + "</b>")
                        + strAgentName
                        + "\n"
                        + Html.fromHtml("<b>" + Hcode + "</b>")
                        + strAgentCode
                        + "\n"
                        + Html.fromHtml("<b>" + Hmobile + "</b>")
                        + strAgentMobileNo
                        + "\n"
                        + "\n"
                        + "You are requested to get in touch with your CIF to complete the policy purchase at the earliest."
                        + "\n"
                        + "Please note that this quotation is valid for a period of 30 days the date of generation,subject to no changes in the input values/Age or Regulations."
                        + "\n"
                        + "\n"
                        + "For any other assistance:"
                        + "\n"
                        + Html.fromHtml("<b>" + strCall + "</b>")
                        + "Toll Free No.1800 267 9090 (Between 9:00 am to 9:00 pm)"
                        + "\n"
                        + Html.fromHtml("<b>" + strEmail + "</b>")
                        + "info@sbilife.co.in"
                        + "\n"
                        + "\nRegards,\n"
                        + "SBI LIFE INSURANCE COMPANY LTD.\n"
                        + "Registered & Corporate Office: SBI Life Insurance Co. Ltd,\n"
                        + "Natraj, M.V. Road & Western Express Highway Junction, Andheri (East),\n"
                        + "Mumbai - 400 069.IRDAI Registration no. 111.\n"
                        + "website: www.sbilife.co.in | Email: info@sbilife.co.in | CIN: L99999MH2000PLC129113 | Toll Free: 1800 267 9090 (Between 9:00 AM & 9:00 PM)\n"
                        + "\n"
                        + Html.fromHtml("<b><u>" + strDOC_Check_List + "<u></b>")
                        + "\n"
                        + "\n"
                        + "Please keep the self attested copies of the following ready:-"
                        + "\n"
                        + "\n"
                        + Html.fromHtml("<b>" + strIdenty + "</b>")
                        + "PAN Card/ Driving Licence/ Aadhar Card/ Passport"
                        + "\n"
                        + Html.fromHtml("<b>" + strAge + "</b>")
                        + " PAN Card/ Birth Certificate, School / College Certificate/ Passport/ Driving License/ Others"
                        + "\n"
                        + Html.fromHtml("<b>" + strAddress + "</b>")
                        + " Bank Account Statement/ Electricity Bill/ Letter from Recognized Public Authority/ Ration Card/ Telephone Bill"
                        + "\n"
                        + Html.fromHtml("<b>" + strIncome + "</b>")
                        + " Employer Certificate/ Income Tax Returns/ Assessment Order/ Others"
                        + "\n" + Html.fromHtml("<b>" + strInternet + "</b>")
                        + "ready for making the Premium Payment."
                        + "\n" + "\n"
                        + strSystem + "\n" + Disclaimer;
            }

            mypath = new StorageUtils().createFileToAppSpecificDir(
                    context, QuatationNumber + "P01.pdf");

            sendMail(strProposerEmailId, "SBI Life Quotation No "
                            + QuatationNumber + " for your " + strPlanName, emailBody,
                    mypath, Graphical_NACBI_File);

           /* Toast.makeText(context,
                    "Benefit Illustrator PDF Has Been Sent to Your Email Id",
                    Toast.LENGTH_SHORT).show();*/
        } catch (Exception e) {
            Toast.makeText(context,
                    "There was a problem sending the email.", Toast.LENGTH_LONG)
                    .show();
            Log.e("MailApp", "Could not send email", e);
        }
    }

    private class SendMailTask extends AsyncTask<Message, Void, Void> {
        ProgressDialog mProgressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(context,
                    ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog
                    .setTitle(Html
                            .fromHtml("<font color='#00a1e3'><b>Please Wait...<b></font>"));
            mProgressDialog.setMax(100);
            mProgressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(mProgressDialog.isShowing())
            {
                mProgressDialog.dismiss();
            }
           if(new CommonMethods().isNetworkConnected(context)) {
                Toast.makeText(
                        context,
                        "Benefit Illustrator PDF Has Been Sent to Your Email Id",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(
                        context,
                        "Benefit Illustrator PDF will be emailed when user is online",
                        Toast.LENGTH_SHORT).show();

            }
            if(!UIN_NO.equalsIgnoreCase("Dashboard"))
            {
                gotoNeedAnalysisHomeDialog("URN  : " + UIN_NO + "\n\n Details Sync Succesfully");
            }
        }

        @Override
        protected Void doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private Message createMessage(String email, String subject,
                                  String messageBody, Session session, File FileName, File filelocation_Graphical)
            throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("sbilconnectlife@sbi-life.com"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(
                email));
        message.setSubject(subject);
        message.setText(messageBody);
        Multipart mp = new MimeMultipart();
        if (FileName != null) {
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setText(messageBody);
            MimeBodyPart mbp2 = new MimeBodyPart();
            FileDataSource fds = new FileDataSource(FileName);
            mbp2.setDataHandler(new DataHandler(fds));
            mbp2.setFileName(fds.getName());
            mp.addBodyPart(mbp1);
            mp.addBodyPart(mbp2);
        }
        if (filelocation_Graphical != null) {

            MimeBodyPart mbp3 = new MimeBodyPart();
            FileDataSource fds1 = new FileDataSource(filelocation_Graphical);
            mbp3.setDataHandler(new DataHandler(fds1));
            mbp3.setFileName(fds1.getName());
            mp.addBodyPart(mbp3);
        }
        message.setContent(mp);
        return message;
    }

    private void sendMail(String email, String subject, String messageBody,
                          File FileName, File filelocation_Graphical) {
        Session session = new CommonMethods().createSessionObject();

        try {
            Message message = createMessage(email, subject, messageBody,
                    session, FileName, filelocation_Graphical);
            new SendMailTask().execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void gotoNeedAnalysisHomeDialog(String message) {

        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_with_ok_button);
            dialog.setCancelable(false);
            TextView text = dialog.findViewById(R.id.tv_title);
            text.setText(message);
            Button dialogButton = dialog.findViewById(R.id.bt_ok);
            dialogButton.setText("Ok");
            dialogButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();

                    Intent i = new Intent(context,
                            NewBusinessHomeGroupingActivity.class);
                    context.startActivity(i);
                    NeedAnalysisActivity.URN_NO = "";
                    OthersProductListActivity.URNNumber = "";
                    OthersProductListActivity.groupName = "";
                    ((AppCompatActivity) context).finish();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
