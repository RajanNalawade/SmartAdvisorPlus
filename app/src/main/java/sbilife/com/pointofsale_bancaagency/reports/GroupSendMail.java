package sbilife.com.pointofsale_bancaagency.reports;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

/**
 * Created by O0411 on 18/09/2017.
 */

class GroupSendMail {

    private Context mContext;
    private CommonMethods mCommonMethods;

    public void mailRequirement(Context mContext, File filePath, String strQuoateProposalNo, String strEmailIds, String strSubject){

        try {

            this.mContext = mContext;
            mCommonMethods = new CommonMethods();
            String strQuoateProposalNo1 = strQuoateProposalNo;
            File filePath1 = filePath;
            String strEmailIds1 = strEmailIds;
            String strSubject1 = strSubject;

            String strMailBody = "";

            strMailBody = "Dear Team, "
                    + "\n"
                    + "\n"
                    + "Kindly find the attachement.";

            sendMail(strEmailIds, strSubject, strMailBody, filePath);

           // mCommonMethods.showToast(mContext, "Mail has been send to your mail id");

        } catch (Exception e) {
            Toast.makeText(mContext,
                    "There was a problem sending the email.", Toast.LENGTH_LONG)
                    .show();
            Log.e("MailApp", "Could not send email", e);
        }

    }

    private void sendMail(String email, String subject, String messageBody,
                          File FileName) {
        Session session = mCommonMethods.createSessionObject();

        try {
            Message message = createMessage(email, subject, messageBody,
                    session, FileName);
            new SendMailTask().execute(message);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private Session createSessionObject() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.sbi-life.com");

        //26-05-2017 po
        properties.put("mail.smtp.port", "587");

        return Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        "sbilconnectlife@sbi-life.com", "sky@12345");
            }
        });
    }*/

    private Message createMessage(String email_ids, String subject,
                                  String messageBody, Session session, File FileName)
            throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("sbilconnectlife@sbi-life.com"));

        String[] ids = email_ids.split(";");
        InternetAddress[] recipientAddress = new InternetAddress[ids.length];

        for (int i = 0; i < ids.length; i++){
            recipientAddress[i] = new InternetAddress(ids[i]);
        }

        message.addRecipients(Message.RecipientType.TO, recipientAddress);
        message.setSubject(subject);
        message.setText(messageBody);
        Multipart mp = new MimeMultipart();
        // message.setFileName(FileName);
        if (FileName != null) {
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setText(messageBody);
            MimeBodyPart mbp2 = new MimeBodyPart();
            FileDataSource fds = new FileDataSource(FileName);
            mbp2.setDataHandler(new DataHandler(fds));
            mbp2.setFileName(fds.getName());
            mp.addBodyPart(mbp1);
            mp.addBodyPart(mbp2);
//			message.setContent(mp);
        }

        message.setContent(mp);
        return message;
    }

    private class SendMailTask extends AsyncTask<Message, Void, Void> {
        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {

            mProgressDialog = new ProgressDialog(mContext,
                    ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog
                    .setTitle(Html
                            .fromHtml("<font color='#00a1e3'><b>Please Wait...<b></font>"));
            mProgressDialog.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();

            if (mCommonMethods.isNetworkConnected(mContext)) {

                mCommonMethods.showToast(mContext, "Mail has been sent to your Emial id");
            } else {
                mCommonMethods.showToast(mContext, "Mail will be emailed when user is online");
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
}
