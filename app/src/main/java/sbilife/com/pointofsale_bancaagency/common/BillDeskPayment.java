package sbilife.com.pointofsale_bancaagency.common;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

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

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.home.CarouselHomeActivity;

public class BillDeskPayment extends AppCompatActivity implements OnItemClickListener{


	WebView web;
	TextView tv_message;
	String NAMESPACE = "http://tempuri.org/";
	String URL = ServiceURL.SERVICE_URL;
	String SOAP_ACTION_PAYMENT_STATUS = "http://tempuri.org/checkRpPaymentStatus";
	String METHOD_NAME_PAYMENT_STATUS = "checkRpPaymentStatus";

	private AsyncCheckPaymentStatus servicePaymentStatus;

	String strPayment_Status = "";
	ProgressDialog progressDialog;
	Context mContext = this;
	String todaydatetime;

	//Input
	String PolicyNumber;	
	String PremiumAmount;
	String Name="";
	String DueDate="";
	String PaymentDateAndTime="";
	String trnRefNo="";
	String status="";
	String paymentMode="",mobileNo="";
	boolean isBulletPay=false;


	//Mail variables
	String emailBody="";
	String strCCEmailId="", strToEmailId="";

	DecimalFormat currencyFormat;
	private StorageUtils mStorageUtils;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.billdeskpayment);

		web = (WebView) findViewById(R.id.wv_paymnet);
		tv_message = (TextView)findViewById(R.id.tv_message);
		mStorageUtils = new StorageUtils();

		web.setWebViewClient(new myWebClient());
		web.getSettings().setJavaScriptEnabled(true);

		tv_message.setText(Html.fromHtml("Please <b><font color='red'>do not</font></b> click on the &apos;Proceed&apos; button below, till you get a Success / Failure message of the transaction."));

		//Todays date
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyyhhmmss");
		todaydatetime = sdf.format(c.getTime());


		currencyFormat = new DecimalFormat("#.00");

		Intent i = getIntent();



		//      PolicyNumber = "123456773353";
		//		PremiumAmount = "1.00";


		PolicyNumber = i.getStringExtra("policyNo");
		PremiumAmount = i.getStringExtra("PremiumAmt");
		Name = i.getStringExtra("name");
		DueDate = i.getStringExtra("premiumDueDate");
		paymentMode = i.getStringExtra("paymentMode");
		mobileNo = i.getStringExtra("policyMobileNo");
		isBulletPay = i.getBooleanExtra("BulletPay",false);

		//		System.out.println("isBulletPay" + isBulletPay);

		//		System.out.println("PolicyNumber"+PolicyNumber);
		//		System.out.println("PremiumAmount" +PremiumAmount);
		//		System.out.println("Name" + Name);
		//		System.out.println("DueDate"+DueDate);


		String strFinalMainMsg = "";
		String strFinalServiceName = "";
		if(paymentMode.equalsIgnoreCase("BillDesk")){
			strFinalMainMsg = PolicyNumber + "|" + PremiumAmount + "|"
				+ "ConnectLife-EASYACC";
			strFinalServiceName = "EasyPayrequest";
		}else if(paymentMode.equalsIgnoreCase("Paytm")){
			strFinalMainMsg = PolicyNumber + "|" + PremiumAmount + "|"
					+ "ConnectLife-EASYACC"+"|"+ mobileNo;
			strFinalServiceName = "EasyPayRequestPaytm";
		}
//		+ "var to = 'https://125.18.9.94/EasyPayrequest.aspx?REQ_CHANNEL="
		String finalUrl = "javascript:"
					+ "var to = 'https://sbilposservices.sbilife.co.in/"+strFinalServiceName+".aspx?REQ_CHANNEL="
				+ strFinalMainMsg + "';" + "var p = {param:'" + 12345 + "'};"
				+ "var myForm = document.createElement('form');"
				+ "myForm.method='post' ;" + "myForm.action = to;"
				+ "for (var k in p) {"
				+ "var myInput = document.createElement('input') ;"
				+ "myInput.setAttribute('type', 'hidden');"
				+ "myInput.setAttribute('name', 'msg') ;"
				+ "myInput.setAttribute('value', '" + strFinalMainMsg + "');"
				+ "myForm.appendChild(myInput) ;" + "}"
				+ "document.body.appendChild(myForm) ;" + "myForm.submit() ;"
				+ "document.body.removeChild(myForm) ;";

		web.loadUrl(finalUrl);


	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}



	/*onclick method*/
	public void btn_click(View v) {
		check_payment_status();
		// Intent intent = new Intent(this, Integrate_serviceActivity.class);
		// startActivity(intent);
	}


	public void check_payment_status() {
		progressDialog = new ProgressDialog(this);
		String Message = "Loading. Please wait...";
		progressDialog.setMessage(Message);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		//		progressDialog.setCancelable(true);
		//
		//		progressDialog.setButton("Cancel",
		//				new DialogInterface.OnClickListener() {
		//			public void onClick(DialogInterface dialog, int which) {
		//
		//				servicePaymentStatus.cancel(true);
		//				progressDialog.dismiss();
		//			}
		//		});

		progressDialog.setMax(100);
		progressDialog.show();

		servicePaymentStatus = new AsyncCheckPaymentStatus();
		servicePaymentStatus.execute();

	}

	/**************************************** PDF creation start ********************************************************/	
	public boolean createPdf() {
		try {



			String BASE_FONT_BOLD = "Trebuchet MS B";	
			//				Font titlefontSmall = FontFactory.getFont(BASE_FONT_BOLD, 12, Font.UNDERLINE);
			//			 
			//				Font sub_headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 11,Font.BOLD);
			//
			//				Font headerBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,	Font.BOLD, BaseColor.WHITE);
			//				Font small_bold = new Font(Font.FontFamily.TIMES_ROMAN, 10,	Font.BOLD);
			Font small_normal = new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.NORMAL);
			//				Font normal_italic = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.ITALIC);
			//				Font normal_bolditalic =  new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLDITALIC);
			//				Font normal_underline =  new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.UNDERLINE);

			Font normal_bold = new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD);
			//				Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.NORMAL);
			//				Font normal_bold_underline = new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD | Font.UNDERLINE);
			//				Font footer_normal = new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.NORMAL);

			String strFileName = PolicyNumber + todaydatetime +".pdf";

			File mypath = mStorageUtils.createFileToAppSpecificDir(mContext, strFileName);

			Document document = new Document(PageSize.A4, 20, 20, 20, 20);
			PdfWriter pdf_writer = null;
			pdf_writer = PdfWriter.getInstance(document, new FileOutputStream(mypath.getAbsolutePath()));
			//pdf_writer.setPageEvent(new HeaderAndFooter());

			// float[] columnWidths_4 = { 2f, 1f, 2f, 1f };

			document.open();

			InputStream ims = getAssets().open("logo.png");
			Bitmap bmp = BitmapFactory.decodeStream(ims);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
			Image image = Image.getInstance(stream.toByteArray());
			image.scalePercent(50f);



			PdfPTable table1 = new PdfPTable(1);
			table1.setWidths(new float []{2f});
			table1.setWidthPercentage(70);
			PdfPCell cell;
			cell = new PdfPCell(new Phrase("Renewal Premium Acknowledgment for Online Payment",normal_bold));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell);

			PdfPTable table2 = new PdfPTable(1);
			table2.setWidths(new float []{2f});
			table2.setWidthPercentage(70);
			cell = new PdfPCell(new Phrase("This is to acknowledge the successful payment of Rs. "+ currencyFormat.format(Double.parseDouble(PremiumAmount)) +" through Online Renewal Premium payment. The details are as follows :",small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table2.addCell(cell);


			PdfPTable table3 = new PdfPTable(2);
			table3.setWidths(new float []{2f,2f});
			table3.setWidthPercentage(70);

			//1
			cell = new PdfPCell(new Phrase("Policy Holder Name :",small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table3.addCell(cell);
			cell = new PdfPCell(new Phrase(Name,small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table3.addCell(cell);

			//2
			cell = new PdfPCell(new Phrase("Policy Number :",small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table3.addCell(cell);
			cell = new PdfPCell(new Phrase(PolicyNumber,small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table3.addCell(cell);

			//3
			cell = new PdfPCell(new Phrase("Due Date :",small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table3.addCell(cell);
			cell = new PdfPCell(new Phrase(DueDate,small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table3.addCell(cell);
			//4
			cell = new PdfPCell(new Phrase("Payment Date and Time :",small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table3.addCell(cell);
			cell = new PdfPCell(new Phrase(PaymentDateAndTime,small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table3.addCell(cell);

			//5
			cell = new PdfPCell(new Phrase("Amount Paid :",small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table3.addCell(cell);
			cell = new PdfPCell(new Phrase(currencyFormat.format(Double.parseDouble(PremiumAmount)),small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table3.addCell(cell);

			//6
			cell = new PdfPCell(new Phrase("Transaction Reference No. :",small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table3.addCell(cell);
			cell = new PdfPCell(new Phrase(trnRefNo,small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table3.addCell(cell);

			//7
			cell = new PdfPCell(new Phrase("Transaction Status :",small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table3.addCell(cell);
			cell = new PdfPCell(new Phrase(status,small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table3.addCell(cell);

			String notes ="> Transactions completed before 3.00 pm on a business day will be unitized based on the closing NAV declared or Premium due date whichever is later.\n\n" +
					"> Transactions completed after 3.00 pm will be unitized based on the closing NAV of the next business day or the due date of premium whichever is later. \n\n"+
					"> In case of ULIP policies, the Renewal Premium Receipt will be issued after the utilization of premium\n\n" +
					"> This acknowledgment is electronically generated and does not require any signature.\n\n";



			//8
			cell = new PdfPCell(new Phrase(notes,small_normal));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setColspan(2);
			table3.addCell(cell);


			Paragraph space_para = new Paragraph("");
			Paragraph newline_para = new Paragraph("\n");


			document.add(image);
			document.add(newline_para);

			document.add(space_para);
			document.add(table1);

			document.add(newline_para);
			document.add(space_para);
			document.add(table2);


			document.add(newline_para);
			document.add(space_para);
			document.add(table3);


			document.close();

			//Toast.makeText(mContext, "Receipt created in sdcard/download folder", Toast.LENGTH_SHORT).show();

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(getLocalClassName(), e.toString() + "Error in creating Pdf");
			//				System.out.println("error " + e.getMessage());
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
			return false;		

		}
	}


	/**************************************** PDF creation end ********************************************************/	 



	/****************************************** Mail Start ************************************************************/

	private void sendMail(String CCEmailId, String email, String subject,
			String messageBody, File FileName) {
		Session session = createSessionObject();

		try {
			Message message = createMessage(CCEmailId, email, subject,messageBody, session, FileName);
			new SendMailTask().execute(message);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		//		catch (AddressException e) {
		//			e.printStackTrace();
		//		} catch (MessagingException e) {
		//			e.printStackTrace();
		//		} catch (UnsupportedEncodingException e) {
		//			e.printStackTrace();
		//		}
	}

	private Message createMessage(String CCEmailId, String email,
                                  String subject, String messageBody, Session session, File FileName)
					throws MessagingException, UnsupportedEncodingException {
		Message message = new MimeMessage(session);
		//		message.setFrom(new InternetAddress("sbilconnectlife@sbi-life.com"));
		message.setFrom(new InternetAddress("easyaccessadmin@sbi-life.com"));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
		// message.addRecipient(Message.RecipientType.CC, new InternetAddress(
		// CCEmailId));
		message.setSubject(subject);
		message.setText(messageBody);
		// message.setFileName(FileName);
		if (FileName != null) {
			MimeBodyPart mbp1 = new MimeBodyPart();
			mbp1.setText(messageBody);
			MimeBodyPart mbp2 = new MimeBodyPart();
			FileDataSource fds = new FileDataSource(FileName);
			mbp2.setDataHandler(new DataHandler(fds));
			mbp2.setFileName(fds.getName());
			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mbp1);
			mp.addBodyPart(mbp2);
			message.setContent(mp);
		}

		return message;
	}

	private Session createSessionObject() {
		Properties properties = new Properties();
//		properties.put("mail.smtp.auth", "true");
//		properties.put("mail.smtp.starttls.enable", "false");
//		properties.put("mail.smtp.host", "smtp.sbi-life.com");
//		properties.put("mail.smtp.port", "25");
		properties.put("mail.smtp.host", "webmail.sbi-life.com");
		properties.put("mail.smtp.socketFactory.port", "465");
		properties.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.port", "465");
		return Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				//				return new PasswordAuthentication(
				//						"sbilconnectlife@sbi-life.com", "sky@12345");
				return new PasswordAuthentication(
						"easyaccessadmin@sbi-life.com", "Sbilife@5663");
			}
		});
	}


	private class SendMailTask extends AsyncTask<Message, Void, Void> {
		//private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(mContext);
			String Message = "Sending mail... Please wait...";
			progressDialog.setMessage(Message);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);			
			progressDialog.setMax(100);
			progressDialog.show();


		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			progressDialog.dismiss();

			//Toast.makeText(BillDeskPayment.this,"Mail has Been Sent to Your Email Id", Toast.LENGTH_LONG).show();

			Builder alert = new Builder(mContext);
			alert.setTitle("Payment Successful!!!");

			alert.setMessage("Thank You for your Payment. Acknowledgement Receipt Has Been Sent to email id " + strToEmailId);
			alert.setPositiveButton("Ok", new OnClickListener() {

				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					/****/
					/*if(isBulletPay)
					{
						Intent i = new Intent(mContext, MainActivity.class);
						//i.putExtra("currentTab", "RenewalPremium");
						i.putExtra("currentTab", "");
						startActivity(i);
					}
					else
					{
						//Go to main page
						Intent intent = new Intent(BillDeskPayment.this, ServicesMain.class);
						intent.putExtra("currentTab","Pay Premium");
						startActivity(intent);
					}*/
/*****/
				}
			});
			alert.show();



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

	/************************************** Mail End ******************************************************************/

	/***************************************** Async Payemnt task ******************************************************/
	public class AsyncCheckPaymentStatus extends 	AsyncTask<String, Void, String>
	{
		private volatile boolean running = true;

		@Override
		protected String doInBackground(String... param) {
			// TODO Auto-generated method stub
			// Get Channel Detail
			//			M_ChannelDetails channelDetail = db.getChannelDetail(agentId);
			//
			//			String email_Id = channelDetail.getEmail_Id();
			//			String mobile_No = channelDetail.getMobile_No();
			if (isNetworkConnected()) 
			{

				running = true;

				try {

					SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME_PAYMENT_STATUS);

					request.addProperty("strPolicyNum", PolicyNumber);					

					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
					envelope.dotNet = true;

					envelope.setOutputSoapObject(request);

					//Utility.allowAllSSL();

					StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
					StrictMode.setThreadPolicy(policy);

					HttpTransportSE androidHttpTranport = new HttpTransportSE(URL);

					androidHttpTranport.call(SOAP_ACTION_PAYMENT_STATUS,envelope);
					Object response = envelope.getResponse();

					strPayment_Status = response.toString();




				} 
				catch (Exception e) {
					e.printStackTrace();
					running = false;

					return "Server not responding...";
				}

			} else
				return "Please Activate Internet connection";

			return null;

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			try
			{


				if (progressDialog.isShowing())
					progressDialog.dismiss();

				if (running != false) {


					//					System.out.println("strPayment_Status" + strPayment_Status);

					/**
					 * Test
					 */
//					strPayment_Status = "<PolicyData> <Table> <EASYACCESS_PAYMENT_DETAILS_ID>3</EASYACCESS_PAYMENT_DETAILS_ID> <TXN_REF_NO>MSCC3422325557</TXN_REF_NO> <TXN_TIME>2014-08-01T16:37:37-07:00</TXN_TIME> <TXN_STATUS>Success </TXN_STATUS> </Table> </PolicyData></string> ";

					if (strPayment_Status.contains("Success"))
					{


						ParseXML prsObj =  new ParseXML();
						//strPayment_Status = prsObj.parseXmlTag(result, "Table");
						trnRefNo = prsObj.parseXmlTag(strPayment_Status, "TXN_REF_NO");
						PaymentDateAndTime = prsObj.parseXmlTag(strPayment_Status, "TXN_TIME");
						status = "Success";


						//Toast.makeText(mContext, "Thanks For Payment",Toast.LENGTH_LONG).show();


						/*emailBody = "Dear customer,"+
								"\n\nWe thank you for the payment made today for your policy "+ PolicyNumber +". Please find attached your payment acknowledgement."+
								"\n\nThe Renewal Premium Receipt will be sent after three working days from the date of adjustment of premium. In case of ULIP policies, the premium is adjusted on due date or on the date of transaction whichever is later."+
								//"\n\nIn our continuous endeavour to offer you world class services at your doorstep and as an environment friendly organization, team SBI Life presents to you 'GO GREEN'. An initiative to preserve the environment by giving you an option to go paper-less by registering for e-statements and join our efforts to save trees."+
								//"\n\nPlease click on the link provided below to register and start receiving your renewal premium receipts and other communications to your registered email ID."+
								//"\n https://mypolicy.sbilife.co.in/GoGreen/GoGreenWebsiteRegister.aspx"+
								//"\n\nIf your email ID is not already registered with us, you can register it by sending an SMS from your registered mobile number as per the template below. You may then register for Go Green after 24 hours."+
								//"\n\nTo register type myemail <space> <11 digits policy number> <space> <email ID> and send it to 56161 or 9250001848. For example if your policy number is 'xxxxxxxxxx' and your email ID is 'abc@xyz.com' you must type 'myemail xxxxxxxxxxx abc@xyz.com'"+
								"\n\nIn case of any queries please feel free to get in touch with us at any of the touch points mentioned below."+
								"\n Thank you for being a valued customer."+
								"\n\nWarm regards,"+
								"\n  SBI Life"+
								"\n\n\nReach us at: www.sbilife.co.in | Toll free: 1800-267-9090 | Email @ info@sbilife.co.in | SBI Life Branches" +
								"\nThis is a system generated email. We request you not to reply to this message.";


						createPdf();

						File folder = new File(strfilepath);

						File mypath = new File(folder, strFileName);

						//strToEmailId = ApplicationTemp.getEmail();


						//						System.out.println("strToEmailId" + strToEmailId);

						*//**
						 * Test
						 *//*
//						strToEmailId = "akshaya.mirajkar@sbilife.co.in";

						strCCEmailId = "";

						sendMail(strCCEmailId, strToEmailId, "Your SBI Life Renewal Premium Receipt " + PolicyNumber, emailBody,mypath);*/


						// openpdf();

						//Temp
						//					Intent intent = new Intent(BillDeskPayment.this,ServicesMain.class);
						//					intent.putExtra("currentTab","Pay Premium");
						//					startActivity(intent);

						//send receipt
						//					Intent intent = new Intent(BillDeskPayment.this,ServicesMain.class);
						//					intent.putExtra("currentTab","Pay Premium");
						//					startActivity(intent);

						Builder alert = new Builder(mContext);
						alert.setTitle("Payment Successful!!!");

						//alert.setMessage("Thank You for your Payment. Acknowledgement Receipt Has Been Sent to email id " + strToEmailId);
						alert.setPositiveButton("Ok", new OnClickListener() {

							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								/****/
								Intent i = new Intent(BillDeskPayment.this, CarouselHomeActivity.class);
								i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
								startActivity(i);
/*****/
							}
						});
						alert.show();



					}
					else
					{
						//Toast.makeText(mContext, "Your Payment is Unsuccessful.",Toast.LENGTH_LONG).show();

						Builder alert = new Builder(mContext);
						alert.setTitle("Payment Failure !!!");

						alert.setMessage("Your Payment is Unsuccessful.");
						alert.setPositiveButton("Ok", new OnClickListener() {

							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub


							}
						});
						alert.show();

					}
				} 
				else 
				{



					Builder alert = new Builder(mContext);
					alert.setTitle("Internet Connection Problem..try after some time..");

					alert.setMessage("Your Payment is Unsuccessful.");
					alert.setPositiveButton("Ok", new OnClickListener() {

						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub

						}
					});
					alert.show();



				}


			}
			catch(Exception e)
			{
			}
		}

		private boolean isNetworkConnected() {
			ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(mContext.CONNECTIVITY_SERVICE);
			NetworkInfo ni = cm.getActiveNetworkInfo();
			if (ni != null && ni.isConnected()) {
				// There are active networks.
				return true;
			} else
				return false;
		}

	}
	/***************************************** Async Payemnt task ******************************************************/


//	/******************** SSL certificate start *****************************************************************/
//	private static TrustManager[] trustManagers;
//
//	public static class _FakeX509TrustManager implements javax.net.ssl.X509TrustManager {
//		private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[] {};
//
//		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
//
//		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
//
//		public boolean isClientTrusted(X509Certificate[] chain) {
//			return (true);
//		}
//
//		public boolean isServerTrusted(X509Certificate[] chain) {
//			return (true);
//		}
//
//		public X509Certificate[] getAcceptedIssuers() {
//			return (_AcceptedIssuers);
//		}
//	}
//
//	public static void allowAllSSL() {
//		javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
//			public boolean verify(String hostname, SSLSession session) {
//				return true;
//			}
//		});
//
//		javax.net.ssl.SSLContext context = null;
//
//		if (trustManagers == null) {
//			trustManagers = new javax.net.ssl.TrustManager[] { new _FakeX509TrustManager() };
//		}
//
//		try {
//			context = javax.net.ssl.SSLContext.getInstance("TLS");
//			context.init(null, trustManagers, new SecureRandom());
//		} catch (NoSuchAlgorithmException e) {
//			Log.e("allowAllSSL", e.toString());
//		} catch (KeyManagementException e) {
//			Log.e("allowAllSSL", e.toString());
//		}
//		javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
//	}
//
//
//	/******************** SSL certificate end*****************************************************************/



	/********************** webview class start ************************************************/


	public class myWebClient extends WebViewClient {
		// ProgressDialog progressDialog;

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub

			view.loadUrl(url);

			return true;

		}

		// Show loader on url load
		// @Override
		// public void onLoadResource(WebView view, String url) {
		// if (progressDialog == null) {
		// // in standard case YourActivity.this
		// progressDialog = new ProgressDialog(PaymentProcessActivity.this);
		// progressDialog.setMessage("Loading...");
		// progressDialog.show();
		// }
		// }

		@Override
		public void onPageFinished(WebView view, String url) {
			// try {
			// if (progressDialog.isShowing()) {
			// progressDialog.dismiss();
			// progressDialog = null;
			// }
			// } catch (Exception exception) {
			// exception.printStackTrace();
			// }
		}

//		@Override
//		public void onReceivedSslError(WebView view, final SslErrorHandler handler,SslError error) {
////			handler.proceed(); // Ignore SSL certificate errors
//			
//			 final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//			    builder.setMessage("Proceed for Payment?");
//			    builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
//			        @Override
//			        public void onClick(DialogInterface dialog, int which) {
//			        	handler.proceed();
//			        }
//			    });
//			    builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//			        @Override
//			        public void onClick(DialogInterface dialog, int which) {
//			        	handler.cancel();
//			        }
//			    });
//			    final AlertDialog dialog = builder.create();
//			    dialog.show();
//		}
		
		  @Override
		  public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		  super.onReceivedError(view, errorCode, description, failingUrl);

		  }

	}	



	/***************** webview class end ********************************************************/



}



