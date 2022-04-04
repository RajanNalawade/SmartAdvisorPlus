package sbilife.com.pointofsale_bancaagency;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore.MediaColumns;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import sbilife.com.pointofsale_bancaagency.ParseXML.XMLHolderBankList;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.SendSmsAsyncTask;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.new_bussiness.RenewalPremiumNBBean;
@SuppressWarnings("deprecation")
public class RenewalPremiumAddActivity extends AppCompatActivity{
	private  final String NAMESPACE = "http://tempuri.org/";
	private  final String URl = ServiceURL.SERVICE_URL;

    private int RESULT_LOAD_IMAGE = 100;
	private int CAMERA_CAPTURE = 101;

	//private String renewalFolderName = "Renewal Payment";

    private EditText et_rene_paymnt_policy_no, et_rene_paymnt_dob,
			et_rene_paymnt_micr_code, et_rene_paymnt_mobile_num,
			et_rene_accnt_no, et_rene_cheque_no, et_rene_cheque_date,
			et_rene_cheque_amount;
	
	private EditText et_rene_paymnt_bank_name, et_rene_paymnt_branch_name;
	
	//private RadioGroup rg_rp_add;

    private TextView tv_rene_paymnt_bank, tv_rene_paymnt_branch;
	
	private Spinner sp_rene_pay_mode, sp_rene_pay_type, sp_rene_payment_type;

    private String policy_no, dob, micr_code, mob_num, account_no, cheque_no,
			cheque_date, cheque_amount, payMode, payType, paymentType;
	
	private String bank_name, branch_name, bank_name_selctd = "";

	private String cif_number, user_type, user_password, user_email_id, user_mob;

	private TableLayout tb_amount, tb_doc_image;

	private LinearLayout ll_cheque_details;

	//private View view_after_btn_submit;

	private TextView txt_rene_pay_amount_val, txt_rene_due_date_val;

	private int dob_year = 1997, dob_month = 0, dob_day = 01;
	private int curr_year, curr_month, curr_day;

	private DatabaseHelper dbhelper;

	private ProgressDialog pd;
	
	private boolean isMICR;//, bank_name_validation, micrValidation = false;

    private String errCode_xml;
    private String errDes_xml;
    private String amount_xml;
    private String due_date_xml;
    private String cus_name_xml = "";//mob_num_xml,

	private String save_rp_nb_response, bank_list_response = "";
	private Context context;
	private CommonMethods mCommonMethods;
	private StorageUtils mStorageUtils;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.renewal_premium_add);
		context = this;
		dbhelper = new DatabaseHelper(context);
		mCommonMethods = new CommonMethods();
		mStorageUtils = new StorageUtils();

		et_rene_paymnt_policy_no = findViewById(R.id.et_rene_paymnt_policy_no);
		et_rene_paymnt_dob = findViewById(R.id.et_rene_paymnt_dob);
		et_rene_paymnt_micr_code = findViewById(R.id.et_rene_paymnt_micr_code);
		et_rene_paymnt_mobile_num = findViewById(R.id.et_rene_paymnt_mobile_num);

		et_rene_accnt_no = findViewById(R.id.et_rene_accnt_no);
		et_rene_cheque_no = findViewById(R.id.et_rene_cheque_no);
		et_rene_cheque_date = findViewById(R.id.et_rene_cheque_date);
		et_rene_cheque_amount = findViewById(R.id.et_rene_cheque_amount);
		
	
		//rg_rp_add = (RadioGroup) findViewById(R.id.rg_rp_add);

        RadioButton rb_rp_add_yes = findViewById(R.id.rb_rp_add_yes);
        RadioButton rb_rp_add_no = findViewById(R.id.rb_rp_add_no);
		
		tv_rene_paymnt_bank = findViewById(R.id.tv_rene_paymnt_bank);
		tv_rene_paymnt_branch = findViewById(R.id.tv_rene_paymt_branch);
		
		et_rene_paymnt_bank_name = findViewById(R.id.et_rene_paymnt_bank_name);
		et_rene_paymnt_branch_name = findViewById(R.id.et_rene_paymnt_branch_name);

		tb_amount = findViewById(R.id.tb_amount);
		tb_doc_image = findViewById(R.id.tb_doc_image);

		ll_cheque_details = findViewById(R.id.ll_cheque_details);

		//view_after_btn_submit = findViewById(R.id.view_after_btn_submit);

		txt_rene_pay_amount_val = findViewById(R.id.txt_rene_pay_amount_val);
		txt_rene_due_date_val = findViewById(R.id.txt_rene_due_date_val);
		
		sp_rene_pay_mode = findViewById(R.id.sp_rene_paymode);
		sp_rene_pay_type = findViewById(R.id.sp_rene_pay_type);
		sp_rene_payment_type = findViewById(R.id.sp_rene_payment_type);

        Button btn_submit = findViewById(R.id.btn_submit);


        Button btn_rene_premium_upload = findViewById(R.id.btn_rene_premium_upload);

		// Block editing on date fields
		et_rene_paymnt_dob.setKeyListener(null);
		et_rene_cheque_date.setKeyListener(null);
		
		et_rene_paymnt_policy_no.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					policyNumberValidation();
				}							
			}
		});
		
		// MICR Radio button 'Yes'
		rb_rp_add_yes.setOnClickListener(new OnClickListener() {			
			
			public void onClick(View v) {
				isMICR = true;				
				et_rene_paymnt_micr_code.setVisibility(View.VISIBLE);
				tv_rene_paymnt_bank.setVisibility(View.GONE);
				tv_rene_paymnt_branch.setVisibility(View.GONE);
				et_rene_paymnt_bank_name.setVisibility(View.GONE);
				et_rene_paymnt_branch_name.setVisibility(View.GONE);
				
			}
		});
		
		// MICR Radio button 'No'
		rb_rp_add_no.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				isMICR = false;
				et_rene_paymnt_micr_code.setVisibility(View.GONE);
				tv_rene_paymnt_bank.setVisibility(View.VISIBLE);
				tv_rene_paymnt_branch.setVisibility(View.VISIBLE);
				et_rene_paymnt_bank_name.setVisibility(View.VISIBLE);
				et_rene_paymnt_branch_name.setVisibility(View.VISIBLE);
				
			}
		});

		// Call DatePicker for DOB (dd/mm/yyyy)
		et_rene_paymnt_dob
				.setOnFocusChangeListener(new OnFocusChangeListener() {
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							// context.showDialog(999);
							DatePickerDialog d = new DatePickerDialog(
									context, R.style.AppBaseTheme,
									dob_listener, dob_year, dob_month, dob_day);
							d.show();
						}
					}
		});

		// Call DatePicker for DOB (dd/mm/yyyy)
		et_rene_paymnt_dob.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// context.showDialog(999);
				DatePickerDialog d = new DatePickerDialog(context,
						R.style.AppBaseTheme, dob_listener, dob_year,
						dob_month, dob_day);
				d.show();
			}
		});

		// Call DatePicker for Cheque Date(dd/mm/yyyy)
		et_rene_cheque_date
				.setOnFocusChangeListener(new OnFocusChangeListener() {
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							final Calendar c = Calendar.getInstance();
							curr_year = c.get(Calendar.YEAR);
							curr_month = c.get(Calendar.MONTH);
							curr_day = c.get(Calendar.DAY_OF_MONTH);
							DatePickerDialog d = new DatePickerDialog(
									context, R.style.AppBaseTheme,
									cheque_date_listener, curr_year,
									curr_month, curr_day);
							d.show();

						}
					}
		});

		// Call DatePicker for Cheque Date(dd/mm/yyyy)
		et_rene_cheque_date.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				final Calendar c = Calendar.getInstance();
				curr_year = c.get(Calendar.YEAR);
				curr_month = c.get(Calendar.MONTH);
				curr_day = c.get(Calendar.DAY_OF_MONTH);
				DatePickerDialog d = new DatePickerDialog(context,
						R.style.AppBaseTheme, cheque_date_listener, curr_year,
						curr_month, curr_day);
				d.show();
			}
		});

		et_rene_paymnt_micr_code
				.setOnFocusChangeListener(new OnFocusChangeListener() {

					public void onFocusChange(View v, boolean hasFocus) {
						
						if(!hasFocus){
							micrValidation();
						}						
					}
		});

		et_rene_paymnt_mobile_num
				.setOnFocusChangeListener(new OnFocusChangeListener() {

					public void onFocusChange(View v, boolean hasFocus) {
						if(!hasFocus){
							mobNoValidation();
						}						
					}
		});

		btn_submit.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				policy_no = et_rene_paymnt_policy_no.getText().toString().trim();
				dob = et_rene_paymnt_dob.getText().toString().trim();
				micr_code = et_rene_paymnt_micr_code.getText().toString()
						.trim();
				mob_num = et_rene_paymnt_mobile_num.getText().toString().trim();
				
				bank_name = et_rene_paymnt_bank_name.getText().toString().trim();
				branch_name = et_rene_paymnt_branch_name.getText().toString().trim();			
				
				policyNumberValidation();
				mobNoValidation();
				
				if(micrValidation()){
					if (policy_no.length() == 11 && dob.length() != 0 && mob_num.length() == 10) {
						
						if(bankNameValidation()){
							// Hide keyboard
							hideKeyboard();

							// Checking internet connection
							if (mCommonMethods.isNetworkConnected(context)) {
								// Call Async task to get RP Premium details
								new GetRPPremiumDetails().execute();
							} 
							else {
								Toast.makeText(context, "Please check your internet connection.",	Toast.LENGTH_SHORT).show();
							}
						}
						else{
							Toast.makeText(context, "Entered Bank Name doesn't exist. Please search again.",Toast.LENGTH_SHORT).show();
						}
						
					} else {
						Toast.makeText(context, "Please fill up all required fields.", Toast.LENGTH_SHORT).show();
					}
				}
				else{
					Toast.makeText(context, "Please fill up all required fields.", Toast.LENGTH_SHORT).show();	
				}
			}
		});
		
		/*
		 * Bank name search button click on edit text
		 */
		et_rene_paymnt_bank_name.setOnTouchListener(new OnTouchListener() {			
		
			public boolean onTouch(View v, MotionEvent event) {
				final int DRAWABLE_RIGHT = 2;
				
				if(event.getAction() == MotionEvent.ACTION_UP){
					if(event.getRawX() >= (et_rene_paymnt_bank_name.getRight() - 
							et_rene_paymnt_bank_name.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())){
						
						bank_name = et_rene_paymnt_bank_name.getText().toString().trim();
						
						if(bank_name.length() > 0){
							if(mCommonMethods.isNetworkConnected(context)){
								
								// Calling Async task to get Bank Names
								new GetBankNames().execute();
							}
							else{
								Toast.makeText(context,
										"Please check your internet connection.",
										Toast.LENGTH_SHORT).show();
							}
						}
						else{
							Toast.makeText(context,
									"Please Enter Bank Name.",
									Toast.LENGTH_SHORT).show();
						}			
						
						//showBankNames();
						
						return true;
					}
				}
				return false;
			}
		});

		/*
		 * btn_rene_premium_camera.setOnClickListener(new OnClickListener() {
		 * public void onClick(View v) {
		 * 
		 * policy_no = getPolicyNumber();
		 * 
		 * if(policy_no != null){ String root_dir =
		 * Environment.getExternalStorageDirectory().toString(); File myDir =
		 * new File(root_dir+"/"+projectName+"/"+renewalFolderName);
		 * 
		 * if(!myDir.exists()){ System.out.println("Dir not exist");
		 * myDir.mkdirs(); }
		 * 
		 * //use standard intent to capture an image Intent captureIntent = new
		 * Intent("android.media.action.IMAGE_CAPTURE"); File temp_file = new
		 * File(myDir,policy_no+".png"); Uri photoPath =
		 * Uri.fromFile(temp_file);
		 * captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoPath);
		 * 
		 * startActivityForResult(captureIntent, CAMERA_CAPTURE); } else{
		 * Toast.makeText(context, "Policy number cannot be blank.",
		 * Toast.LENGTH_SHORT).show(); }
		 * 
		 * } });
		 * 
		 * btn_rene_premium_gallery.setOnClickListener(new OnClickListener() {
		 * public void onClick(View v) {
		 * 
		 * policy_no = getPolicyNumber();
		 * 
		 * if(policy_no != null){ Intent i = new Intent(Intent.ACTION_PICK,
		 * android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		 * startActivityForResult(i, RESULT_LOAD_IMAGE); } else{
		 * Toast.makeText(context, "Policy number cannot be blank.",
		 * Toast.LENGTH_SHORT).show(); } } });
		 * 
		 * btn_rene_premium_view.setOnClickListener(new OnClickListener() {
		 * public void onClick(View v) { //Checking for the existence of the pdf
		 * file if(fileValidation()){ policy_no = getPolicyNumber(); String root
		 * = Environment.getExternalStorageDirectory().toString(); File myDir =
		 * new File(root+"/"+projectName+"/"+renewalFolderName);
		 * if(myDir.exists()){
		 * 
		 * //Show pdf File file_pdf = new File(myDir, policy_no+".pdf"); Uri
		 * path = Uri.fromFile(file_pdf); Intent intent = new
		 * Intent(Intent.ACTION_VIEW); intent.setDataAndType(path,
		 * "application/pdf"); intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 * 
		 * try{ startActivity(intent); } catch(ActivityNotFoundException e){
		 * Toast.makeText(context,
		 * "No application available to view the document.",
		 * Toast.LENGTH_SHORT).show(); } } } } });
		 */

		btn_rene_premium_upload.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				/*policy_no = et_rene_paymnt_policy_no.getText().toString().trim();
				dob = et_rene_paymnt_dob.getText().toString().trim();*/
				micr_code = et_rene_paymnt_micr_code.getText().toString()
						.trim();
				mob_num = et_rene_paymnt_mobile_num.getText().toString().trim();

				account_no = et_rene_accnt_no.getText().toString().trim();
				cheque_no = et_rene_cheque_no.getText().toString().trim();
				cheque_date = et_rene_cheque_date.getText().toString().trim();
				cheque_amount = et_rene_cheque_amount.getText().toString()
						.trim();
				
				payMode = sp_rene_pay_mode.getSelectedItem().toString();
				payType = sp_rene_pay_type.getSelectedItem().toString();
				paymentType = sp_rene_payment_type.getSelectedItem().toString();
				
				policyNumberValidation();
				mobNoValidation();

				if (policy_no.length() == 11 && dob.length() != 0 && mob_num.length() == 10 
						&& account_no.length() != 0 && cheque_no.length() != 0
						&& cheque_date.length() != 0 && cheque_amount.length() != 0
						&& !payMode.equals("Select Pay Mode") && !payType.equals("Select Pay Type")
						&& !paymentType.equals("Select Payment Type")) {

					Toast.makeText(context, "Validation succeeded.",
							Toast.LENGTH_SHORT).show();
					
					if(bankNameValidation()){
						//Checking for cheque no duplication
						if(dbhelper.isDuplicateChequeNo(cheque_no)){
							
							// Checking internet connection
							if (mCommonMethods.isNetworkConnected(context)) {
								
								// Decrypting User details except password
								try {
									cif_number = SimpleCrypto.decrypt("SBIL",
											dbhelper.GetCIFNo());
									user_type = SimpleCrypto.decrypt("SBIL",
											dbhelper.GetUserType());
									user_email_id = SimpleCrypto.decrypt("SBIL",
											dbhelper.GetEmailId());
									user_mob = SimpleCrypto.decrypt("SBIL",
											dbhelper.GetMobileNo());
									user_password = dbhelper.GetPassword();
								} catch (Exception e) {
									e.printStackTrace();
								}
								
								// Async task to save RP details
								new SyncRPDetails().execute();
								
							} else {
								// Saving details to DB
								savingDetailsToDB("0");

								Toast.makeText(context,
										"Please check your internet connection.",
										Toast.LENGTH_SHORT).show();
							}
						}
						
						else{
							Toast.makeText(context,
									"Entered cheque number already exist. Please check again.",
									Toast.LENGTH_SHORT).show();
						}
					}
					else{
						Toast.makeText(context, "Entered Bank Name doesn't exist. Please search again.",Toast.LENGTH_SHORT).show();
					}				

				} else {
					Toast.makeText(context,
							"Please fill up all cheque fields.",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

	}
	
	/*
	 * Alert Dialog to show bank names
	 */
	private void showBankNames(final String[] bankNames){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		
		builder.setTitle("Select Bank Name");
		//Cancel Dialog
		builder.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {			
			
			public void onClick(DialogInterface dialog, int which) {				
			}
		});
		
		builder.setItems(bankNames, new DialogInterface.OnClickListener() {			
			
			public void onClick(DialogInterface dialog, int which) {
				et_rene_paymnt_bank_name.setText(bankNames[which]);
				
				//Setting selected bank name
				bank_name_selctd = bankNames[which].trim();
			}
		});
		final AlertDialog alert = builder.create();
		alert.show();
	}

	// Async task to get Rp premium details
	private class GetRPPremiumDetails extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(context);
			pd.setMessage("Loading");
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

            String METHOD_NAME_GET_RP_PREMIUM_DETAILS = "getRpPremiumDetails";
            SoapObject request = new SoapObject(NAMESPACE,
                    METHOD_NAME_GET_RP_PREMIUM_DETAILS);

			System.out.println("Policy and dob in background: " + policy_no
					+ " and " + dob);

			request.addProperty("strPolicyNo", policy_no);
			request.addProperty("strDob", convertDateFormat(dob));

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
                String SOAP_ACTION_GET_RP_PREMIUM_DETAILS = "http://tempuri.org/getRpPremiumDetails ";
                androidHttpTranport.call(SOAP_ACTION_GET_RP_PREMIUM_DETAILS,
						envelope);

				// if(response.equals("null")){
				SoapPrimitive sa = null;

				sa = (SoapPrimitive) envelope.getResponse();

                String rpPremiumDetails = sa.toString();

				System.out.println("RPPremiumDetails:" + rpPremiumDetails);

				ParseXML prsObj = new ParseXML();

				if (!rpPremiumDetails.equalsIgnoreCase("1")
						|| !rpPremiumDetails.equalsIgnoreCase("0")) {

					rpPremiumDetails = prsObj.parseXmlTag(rpPremiumDetails,
							"PolicyDetails");

					rpPremiumDetails = new ParseXML().parseXmlTag(
                            rpPremiumDetails, "ScreenData");

					if (rpPremiumDetails != null) {
						System.out.println("Premium Details are not null: "
								+ rpPremiumDetails);

						// Error code
						errCode_xml = new ParseXML().parseXmlTag(
                                rpPremiumDetails, "ErrCode");
						System.out.println("Err code:" + errCode_xml + ".");

						if (errCode_xml != null) {

							if (errCode_xml.equals("0")) {
								System.out.println("Err code is 0");

								// Cus name
								cus_name_xml = new ParseXML().parseXmlTag(
                                        rpPremiumDetails, "Sbi_First_Name");
								
								String strLname= new ParseXML().parseXmlTag(
                                        rpPremiumDetails, "Sbi_Middle_Name");
								
								if(strLname != null)
								{
									cus_name_xml += " ";
									cus_name_xml += new ParseXML()
											.parseXmlTag(rpPremiumDetails,
													"Sbi_Middle_Name");
								}

								/*if (!new ParseXML().parseXmlTag(
										rpPremiumDetails, "Sbi_Middle_Name").isEmpty()) {
									cus_name_xml += " ";
									cus_name_xml += new ParseXML()
											.parseXmlTag(rpPremiumDetails,
													"Sbi_Middle_Name");
								}*/
								cus_name_xml += " ";
								cus_name_xml += new ParseXML().parseXmlTag(
                                        rpPremiumDetails, "Sbi_Last_Name");

								System.out.println("Cus name xml:"
										+ cus_name_xml);

								// Due date
								due_date_xml = new ParseXML().parseXmlTag(
                                        rpPremiumDetails, "DueDate");

								// Amount
								amount_xml = new ParseXML().parseXmlTag(
                                        rpPremiumDetails, "GrossAmt");
								amount_xml=convertAmountToInteger(amount_xml);
								// Setting amount value in edittext
//								et_rene_cheque_amount.setText(amount_xml);

								System.out.println("Due date and amount:"
										+ due_date_xml + " and " + amount_xml);

							} else if (errCode_xml.equals("1")) {
								System.out.println("Err code is 1");

								// Error Description
								errDes_xml = new ParseXML().parseXmlTag(
                                        rpPremiumDetails, "ErrDesc");
								if (errDes_xml != null) {
									System.out.println("Error des not null:"
											+ errDes_xml + ".");
								} else {
									System.out.println("Error Des is null.");
								}
							}
						} else {
							// Toast.makeText(context,
							// "Check entered premium number and DOB. Err code is null.",
							// Toast.LENGTH_SHORT).show();
							System.out
									.println("Check entered premium number and DOB.");
						}

					} else {
						// Toast.makeText(context,
						// "Premium Details are null",
						// Toast.LENGTH_SHORT).show();
						System.out.println("Premium Details are null");
					}
				} else {
					errCode_xml = "1";

				}
				// }
				// else{
				// errCode_xml = "1";
				// }

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (pd.isShowing()) {
				pd.dismiss();
			}
			
			if(errCode_xml != null){
				if (errCode_xml.equals("0")) {
					// Setting xml values to text field
					txt_rene_pay_amount_val.setText(amount_xml);
					txt_rene_due_date_val.setText(due_date_xml);
					et_rene_cheque_amount.setText(amount_xml);
					tb_amount.setVisibility(View.VISIBLE);
					ll_cheque_details.setVisibility(View.VISIBLE);
					tb_doc_image.setVisibility(View.VISIBLE);
				} else if (errCode_xml.equals("1")) {
					Toast.makeText(context, errDes_xml, Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(context, "Please check Policy number and DOB.",
							Toast.LENGTH_LONG).show();
				}
			}
			else{
				Toast.makeText(context, "Please check your Policy Number and DOB.",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	// Async task to save Rp premium details
	private class SyncRPDetails extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(context);
			pd.setMessage("Loading");
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			System.out.println("RP details: " + policy_no + ", " + dob + ", "
					+ mob_num + ", " + cus_name_xml + ", " + micr_code + ", "
					+ account_no + ", " + cheque_no + ", " + cheque_amount
					+ ", " + cheque_date + ", " + cif_number + ", " + user_type
					+ ", " + user_email_id + ", " + user_password + ", "
					+ user_mob);

            String METHOD_NAME_SAVE_RP_NB_ACK_DTLS = "saveRpNbAckDtls";
            SoapObject request = new SoapObject(NAMESPACE,
                    METHOD_NAME_SAVE_RP_NB_ACK_DTLS);
			request.addProperty("strPolicyNo", policy_no);
			request.addProperty("strProposalNo", "");
			request.addProperty("StrCustDob", convertDateFormat(dob));
			request.addProperty("strMobNo", mob_num);
			request.addProperty("strCustName", cus_name_xml);
			request.addProperty("strMicrCode", micr_code);
			request.addProperty("strAccNo", account_no);
			request.addProperty("strChqqueNo", cheque_no);
			request.addProperty("StrChDate", convertDateFormat(cheque_date));
			request.addProperty("strCheqAmt", amount_xml);
			request.addProperty("strAdvCode", cif_number);
			request.addProperty("strAdvType", user_type);
			request.addProperty("strPremType", "RP");
			request.addProperty("strCreatedBy", cif_number); 
			
			System.out.println("New details in background:"+payType+", "+payMode+", "+bank_name_selctd+", "+branch_name+", "+paymentType);
			
			request.addProperty("strPACHEQUETYPE", payType);
			request.addProperty("strPAPAYMODE", payMode);
			
			request.addProperty("strPABANKNM", bank_name_selctd);
			request.addProperty("strPABRANCHNM", branch_name);
			
			request.addProperty("strPAPAYMENTTYPE", paymentType);
			
			
			request.addProperty("strEmailId", user_email_id);
			request.addProperty("strMobileNo", user_mob);
			request.addProperty("strAuthKey", user_password);

			System.out.println("User mail id:" + user_email_id);
			System.out.println("User password:" + user_password + ".");

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
                String SOAP_ACTION_SAVE_RP_NB_ACK_DTLS = "http://tempuri.org/saveRpNbAckDtls";
                androidHttpTranport.call(SOAP_ACTION_SAVE_RP_NB_ACK_DTLS,
						envelope);

				SoapPrimitive sa = null;

				sa = (SoapPrimitive) envelope.getResponse();
				
				if(sa != null){
					save_rp_nb_response = sa.toString();					
				}


			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (pd.isShowing()) {
				pd.dismiss();
			}
			
			if(save_rp_nb_response != null){
				// Checking response
				if (save_rp_nb_response.equals("1")) {
					Toast.makeText(context,
							"Details has been synced successfully.",
							Toast.LENGTH_SHORT).show();
					savingDetailsToDB("1");

					// Send SMS
					sendSms();
				} else {
					Toast.makeText(context,
							"Details has been not synced. Please try again.",
							Toast.LENGTH_SHORT).show();
					// savingDetailsToDB("0");
				}
			}
			else{
				Toast.makeText(context,
						"Details has been not synced. Please try again.",
						Toast.LENGTH_SHORT).show();
			}
			
			
		}

	}
	
	//Async Task for Bank Names
	private class GetBankNames extends AsyncTask<Void, Void, Void>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(context);
			pd.setMessage("Loading");
			pd.setCancelable(false);
			pd.show();
		}
		
		@Override
		protected Void doInBackground(Void... params) {


            String METHOD_NAME_GET_BANK_LIST = "getBankList";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_GET_BANK_LIST);
			request.addProperty("strBankName", bank_name.toUpperCase(Locale.ENGLISH));
			
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
                String SOAP_ACTION_GET_BANK_LIST = "http://tempuri.org/getBankList";
                androidHttpTranport.call(SOAP_ACTION_GET_BANK_LIST,
						envelope);

				SoapPrimitive sa = null;

				sa = (SoapPrimitive) envelope.getResponse();
				
				if(sa != null){
					bank_list_response = sa.toString();					
				}


			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			if (pd.isShowing()) {
				pd.dismiss();
			}
			
			if(bank_list_response != null){
				if(bank_list_response.equals("0") || bank_list_response.equals("") || bank_list_response.equals("<NewDataSet />")){
					Toast.makeText(context, "Bank Name couldn't found. Please re-type the Bank Name and search again.", Toast.LENGTH_LONG).show();
				}
				
				else{
					ParseXML parseXML = new ParseXML();
					String[] bankNames;
					int bankCount = 0;
					
					List<XMLHolderBankList> bankList = parseXML.parseNodeElementBankList(parseXML.parseParentNode
								(parseXML.parseXmlTag(bank_list_response, "NewDataSet"), "Table"));
					
					bankNames = new String[bankList.size()];
					
					for(XMLHolderBankList bankName: bankList){
						bankNames[bankCount] = bankName.getBankName();
						bankCount++;
					}
					
					showBankNames(bankNames);
				}
			}
			else{
				Toast.makeText(context, "Bank Name couldn't found. Please re-type the Bank Name and try again.", Toast.LENGTH_LONG).show();
			}			
		}	
		
	}
	
	//MICR Validation
	private boolean micrValidation(){
		if(isMICR){
			if(micr_code != null){
				if(micr_code.length() == 9){
					bank_name = "";
					branch_name = "";
					et_rene_paymnt_micr_code.setError(null);
					return true;
				}
				else{
					et_rene_paymnt_micr_code.setError("MICR code require 9 digits.");
					return false;
				}
			}
			else{
				return false;
			}
			
		}
		else{
			if(bank_name != null && branch_name != null){
				if(bank_name.length() > 0 && branch_name.length() > 0){
					micr_code = "";
					return true;
				}
				else{
					return false;
				}
			}
			else{
				return false;
			}
			
		}
	}
	
	//Bank Name Validation
	private boolean bankNameValidation(){
		if(bank_name_selctd != null){
			return bank_name_selctd.equals(bank_name);
		}
		else{
			return false;
		}
	}
	
	//Policy number validation
	private void policyNumberValidation(){
		if(policy_no != null){
			if(policy_no.length() < 11){
				et_rene_paymnt_policy_no.setError("Policy number require 11 digits.");
			}
			else{
				et_rene_paymnt_policy_no.setError(null);
			}
		}		
	}
	
	//Mobile number validation
	private void mobNoValidation(){
		if (et_rene_paymnt_mobile_num.getText().toString()
				.length() < 10) {
			et_rene_paymnt_mobile_num
					.setError("Mobile number require 10 digits.");
		}
		else{
			et_rene_paymnt_mobile_num.setError(null);
		}
	}

	// Send SMS
    private void sendSms() {
		
		try{
			String str_message =

					"Thank you. Received Cheque No. "
							+ cheque_no
							+ " dated "
							+ cheque_date
							+ " for Rs. "
							+ cheque_amount
							+ " towards premium for policy number "
							+ policy_no
							+ ". T&C apply, refer sbilife.co.in. ~ SBI Life";
					
					/*SmsManager  smsManager = SmsManager.getDefault();
				    
				    ArrayList<String> parts = smsManager.divideMessage(str_message);
				    smsManager.sendMultipartTextMessage("+91"+mob_num, null, parts, null, null);
				    
				    Toast.makeText(context, "SMS has been sent successfully.", Toast.LENGTH_SHORT).show();*/

					if(mCommonMethods.isNetworkConnected(context)){
						SendSmsAsyncTask sendSmsAsyncTask = new SendSmsAsyncTask(context,mob_num,str_message);
						sendSmsAsyncTask.execute();
					}else{
						mCommonMethods.showMessageDialog(context,mCommonMethods.NO_INTERNET_MESSAGE);
					}

		}
		catch(Exception e){
			e.printStackTrace();
			Toast.makeText(context, "SMS sending failed.", Toast.LENGTH_SHORT).show();
		}
	}

	// Saving details to DB
	private void savingDetailsToDB(String isSynced) {
		/*
		 * Saving details to DB
		 */
		RenewalPremiumNBBean premiumNBBean = new RenewalPremiumNBBean();

		try {
			premiumNBBean.setRp_nb_policy_no(policy_no);
			premiumNBBean.setRp_nb_cust_name(cus_name_xml);
			premiumNBBean.setRp_nb_cust_dob(dob);
			premiumNBBean.setRp_nb_micr(micr_code);
			
			premiumNBBean.setRp_nb_bank_name(bank_name_selctd);
			premiumNBBean.setRp_nb_branch_name(branch_name);
			
			premiumNBBean.setRp_nb_cust_mob(mob_num);

			premiumNBBean.setRp_nb_accnt_no(account_no);
			premiumNBBean.setRp_nb_cheque_no(cheque_no);
			premiumNBBean.setRp_nb_cheque_date(cheque_date);
			premiumNBBean.setRp_nb_cheque_amt(amount_xml);
			
			premiumNBBean.setRp_nb_pay_mode(payMode);
			premiumNBBean.setRp_nb_pay_type(payType);
			
			premiumNBBean.setRp_nb_payment_type(paymentType);

			premiumNBBean.setRp_nb_advisor_code(cif_number);
			premiumNBBean.setRp_nb_advisor_type(user_type);

			premiumNBBean.setRp_nb_created_date(getCurrentDateInFormat());
			premiumNBBean.setRp_nb_created_by(cif_number);

			premiumNBBean.setRp_nb_is_rp("RP");
		} catch (Exception e) {
			e.printStackTrace();
		}

		long table_row_id = dbhelper.saveRenewalPremiumDetails(premiumNBBean);
		System.out.println("table_row_id:" + table_row_id);
		if (table_row_id > 0) {
			/*Toast.makeText(context, "Details has been saved in mob db.",
					Toast.LENGTH_SHORT).show();*/

			// Update synced status
			if (isSynced.equals("1")) {
				if (dbhelper.updateRenewalPreSyncFlag(table_row_id, "1") > 0) {
					/*Toast.makeText(context,
							"Syncd status has been changed to 1 in mob db.",
							Toast.LENGTH_SHORT).show();*/
				} else {
					/*Toast.makeText(context,
							"Syncd status has not been changed in mob db.",
							Toast.LENGTH_SHORT).show();*/
				}
			}

			// Refresh the activity to update the list view
			Intent i = new Intent(context, RenewalPremium.class);
			startActivity(i);
			finish();
		} else {
			
		}
	}

	// Convert dd/mm/yyyy to mm/dd/yyyy
    private String convertDateFormat(String dateToBeConvert) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"dd/MM/yyyy");
			Date tempDate = simpleDateFormat.parse(dateToBeConvert);
			SimpleDateFormat outputDateFormat = new SimpleDateFormat(
					"MM/dd/yyyy");
			System.out.println("Output date is = "
					+ outputDateFormat.format(tempDate));
			return outputDateFormat.format(tempDate);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	public boolean fileValidation() {
		policy_no = getPolicyNumber();
		if (policy_no != null) {

				// Checking for pdf file
				File file_pdf = mStorageUtils.createFileToAppSpecificDir(this, policy_no + ".pdf");
				if (file_pdf.exists()) {
					return true;
				} else {
					Toast.makeText(context,
							"Please take document image.", Toast.LENGTH_SHORT)
							.show();
					return false;
				}

		} else {
			Toast.makeText(context, "Policy number cannot be blank.",
					Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	// Checking whether policy number is blank or not
    private String getPolicyNumber() {
		policy_no = et_rene_paymnt_policy_no.getText().toString().trim();
		if (policy_no.trim().length() != 0) {
			return policy_no;
		} else {
			return null;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		
		if (resultCode == AppCompatActivity.RESULT_OK) {
			if (requestCode == RESULT_LOAD_IMAGE) {
				System.out.println("Gallery image succeeded");

				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaColumns.DATA };

				Cursor cursor = context.getContentResolver().query(
						selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String filePath = cursor.getString(columnIndex);
				cursor.close();

				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 4;

				Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

				Matrix matrix = new Matrix();
				matrix.postRotate(getImageOrientation(filePath));
                Bitmap gallery_rotated_bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), matrix, true);

				storeDocument(gallery_rotated_bitmap, policy_no);

			} else if (requestCode == CAMERA_CAPTURE) {

				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 4;

				Bitmap bitmap = BitmapFactory.decodeFile(mStorageUtils.createFileToAppSpecificDir(
						context, policy_no + ".png").getAbsolutePath(), options);

				Matrix matrix = new Matrix();
				matrix.postRotate(getImageOrientation(mStorageUtils.createFileToAppSpecificDir(
						context, policy_no + ".png").getAbsolutePath()));
                Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), matrix, true);

				storeDocument(rotatedBitmap, policy_no);
			}
		}
	}

	// Store Document
    private void storeDocument(Bitmap bitmap, String fileName) {

		// Checking for png file
		File file_png = mStorageUtils.createFileToAppSpecificDir(context, fileName + ".png");
		if (file_png.exists())
			file_png.delete();

		File file = mStorageUtils.createFileToAppSpecificDir(context, fileName + ".pdf");
		if (file.exists()) {
			file.delete();
		}

		try {
			Document document = new Document(PageSize.A4, 50, 50, 50, 50);
			PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
			Image image = Image.getInstance(outputStream.toByteArray());

			/*
			 * //Checking Bitmap and document width and height Rectangle
			 * rectangle = document.getPageSize(); if(image.getWidth() >
			 * rectangle.getWidth() || image.getWidth() > rectangle.getWidth()){
			 * //Bitmap size is greater than document size
			 * image.scaleAbsolute(rectangle.getWidth(), rectangle.getHeight());
			 * }
			 */

			// Setting image to center of the document
			// image.setAbsolutePosition((rectangle.getWidth() -
			// image.getScaledWidth())/2, (rectangle.getHeight() -
			// image.getScaledHeight())/2);

			image.setAlignment(Image.MIDDLE);
			image.scaleToFit(550, 400);
			document.add(image);
			document.close();

			outputStream.flush();
			outputStream.close();

			Toast.makeText(context, "Image saved successfully!!",
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "Problem found while saving image.",
					Toast.LENGTH_SHORT).show();
		}
	}

	// Rotate Image
	private  int getImageOrientation(String imagePath) {
		int rotate = 0;

		try {
			File imageFile = new File(imagePath);
			ExifInterface exif = new ExifInterface(imageFile.getPath());
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rotate;
	}

	/*
	 * @Override protected Dialog onCreateDialog(int id) { // TODO
	 * Auto-generated method stub if (id == 999) { return new
	 * DatePickerDialog(context, dob_listener, dob_year, dob_month,
	 * dob_day); } else if(id == 888){ // set default date final Calendar c =
	 * Calendar.getInstance(); int curr_year = c.get(Calendar.YEAR); int
	 * curr_month = c.get(Calendar.MONTH); int curr_day =
	 * c.get(Calendar.DAY_OF_MONTH);
	 * 
	 * // Create a new instance of DatePickerDialog and return it return new
	 * DatePickerDialog(context, cheque_date_listener, curr_year,
	 * curr_month, curr_day); } return null; }
	 */

	private DatePickerDialog.OnDateSetListener dob_listener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker arg0, int year, int monthOfYear,
				int dayOfMonth) {
			showDOBDate(et_rene_paymnt_dob, year, monthOfYear + 1, dayOfMonth);
			//Setting selected date
			dob_day = arg0.getDayOfMonth();
			dob_month = arg0.getMonth();
			dob_year = arg0.getYear();
		}
	};

	private DatePickerDialog.OnDateSetListener cheque_date_listener = new DatePickerDialog.OnDateSetListener() {

		
		
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			final Calendar c = Calendar.getInstance();

			Calendar selectedDate = new GregorianCalendar(year, monthOfYear,
					dayOfMonth);
			selectedDate.add(Calendar.HOUR_OF_DAY, c.getTime().getHours());
			selectedDate.add(Calendar.MINUTE, c.getTime().getMinutes());
			selectedDate.add(Calendar.SECOND, c.getTime().getSeconds());

			Calendar minRequiredChequeDate = new GregorianCalendar();
			minRequiredChequeDate.add(Calendar.MONTH, -3);
			minRequiredChequeDate.set(Calendar.HOUR_OF_DAY, 0);
			minRequiredChequeDate.set(Calendar.MINUTE, 0);
			minRequiredChequeDate.set(Calendar.SECOND, 1);

			if (selectedDate.before(minRequiredChequeDate)) {
				Toast.makeText(context,
						"Cheque date should not be less than 3 months.",
						Toast.LENGTH_SHORT).show();
				et_rene_cheque_date.setText("");
			} else if (selectedDate.after(getCurrentDate(c))) {
				Toast.makeText(context,
						"Cheque date should not be future date.",
						Toast.LENGTH_SHORT).show();
				et_rene_cheque_date.setText("");
			} else {
				/*Toast.makeText(context, "Proper cheque date.",
						Toast.LENGTH_SHORT).show();*/
				
				//Setting seleted cheque date
				curr_day = dayOfMonth;
				curr_month = monthOfYear;
				curr_year = year;
				
				showDOBDate(et_rene_cheque_date, year, monthOfYear + 1,
						dayOfMonth);
			}
		}
	};

	private void showDOBDate(EditText et, int year, int month, int day) {
//		   et.setText(new StringBuilder().append(day).append("/")
//				      .append(month).append("/").append(year));
		et.setText(new StringBuilder().append((day<10?"0"+(day):day)).append("/").append((month<10?"0"+(month):month))
				.append("/").append(year));
	}

	// Get current date
    private Calendar getCurrentDate(Calendar c) {
		curr_year = c.get(Calendar.YEAR);
		curr_month = c.get(Calendar.MONTH);
		curr_day = c.get(Calendar.DAY_OF_MONTH);

		Calendar currentDate = new GregorianCalendar(curr_year, curr_month,
				curr_day);
		currentDate.add(Calendar.HOUR_OF_DAY, c.getTime().getHours());
		currentDate.add(Calendar.MINUTE, c.getTime().getMinutes());
		currentDate.add(Calendar.SECOND, c.getTime().getSeconds());

		return currentDate;
	}

	// Get current date in format dd/mm/yyyy
    private String getCurrentDateInFormat() {
		Calendar c = Calendar.getInstance();

		curr_year = c.get(Calendar.YEAR);
		curr_month = c.get(Calendar.MONTH);
		curr_day = c.get(Calendar.DAY_OF_MONTH);

		return curr_day + "/" + curr_month
				+ "/" + curr_year;
	}

	// Hide keyboard
	private void hideKeyboard() {
		// Check if no view has focus:
		View view = this.getCurrentFocus();
		if (view != null) {
			InputMethodManager inputManager = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(view.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}


	private String convertAmountToInteger(String amount_xml)
	{
		double temp=Double.parseDouble(amount_xml);
		
		return Math.round(temp)+"";
	}
	

}
