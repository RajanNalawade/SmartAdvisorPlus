package sbilife.com.pointofsale_bancaagency;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;
import sbilife.com.pointofsale_bancaagency.new_bussiness.RenewalPremiumNBBean;
@SuppressWarnings("deprecation")
public class RenewalPremiumNBEdit extends AppCompatActivity{

	//private String renewalFolderName = "Renewal Payment";

    private EditText et_edit_rene_accnt_no;
    private EditText et_edit_rene_cheque_no;
    private EditText et_edit_rene_cheque_date;
    private EditText et_edit_altr_mob_no;

    private String policy_no;

    private int id;

    private int curr_year, curr_month, curr_day;
	
	private DatabaseHelper dbhelper;
	private Context context;
	private StorageUtils mStorageUtils;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	    setContentView(R.layout.renewal_premium_nb_edit);
		mStorageUtils = new StorageUtils();
	    new CommonMethods().setActionbarLayout(this);
	    
	    dbhelper = new DatabaseHelper(this);
	    context = this;
        TextView tv_edit_rene_micr = findViewById(R.id.tv_edit_rene_micr);
        TextView tv_edit_rene_bank_name = findViewById(R.id.tv_edit_rene_bank_name);
        TextView tv_edit_rene_branch_name = findViewById(R.id.tv_edit_rene_branch_name);

        EditText et_edit_rene_policy_no = findViewById(R.id.et_edit_rene_policy_no);
	    et_edit_rene_accnt_no = findViewById(R.id.et_edit_rene_accnt_no);

        EditText et_edit_rene_micr_code = findViewById(R.id.et_edit_rene_micr_code);
        EditText et_edit_rene_bank_name = findViewById(R.id.et_edit_rene_bank_name);
        EditText et_edit_rene_branch_name = findViewById(R.id.et_edit_rene_branch_name);
	    
	    et_edit_rene_cheque_no = findViewById(R.id.et_edit_rene_cheque_no);
	    et_edit_rene_cheque_date = findViewById(R.id.et_edit_rene_cheque_date);
        EditText et_edit_rene_cheque_amt = findViewById(R.id.et_edit_rene_cheque_amt);

        EditText et_edit_rene_pay_mode = findViewById(R.id.et_edit_rene_pay_mode);
        EditText et_edit_rene_pay_type = findViewById(R.id.et_edit_rene_pay_type);

        EditText et_edit_rene_payment_type = findViewById(R.id.et_edit_rene_payment_type);
	    
	    et_edit_altr_mob_no = findViewById(R.id.et_edit_altr_mob_no);
	    
	    //Block editing on date fields
        et_edit_rene_cheque_date.setKeyListener(null);
        
        //Setting current date
        final Calendar c = Calendar.getInstance();
        curr_year = c.get(Calendar.YEAR);
        curr_month = c.get(Calendar.MONTH);
        curr_day = c.get(Calendar.DAY_OF_MONTH);
	    
	    Intent i = getIntent();
	    //RenewalPremiumNBBean bean = (RenewalPremiumNBBean)i.getSerializableExtra("rp_nb_bean");   
	   
	    	//System.out.println("Edit bean:"+bean.getRp_nb_policy_no()+", "+bean.getRp_nb_accnt_no()+", "+bean.getRp_nb_cheque_no()+", "+bean.getRp_nb_cheque_date());
	    	id = i.getIntExtra("id", 0);
	    	policy_no = i.getStringExtra("policy_no");
	    	
	    	if(i.getStringExtra("micr") != null){
		    	
		    	if(i.getStringExtra("micr").equals("")){
		    		tv_edit_rene_bank_name.setVisibility(View.VISIBLE);
		    		tv_edit_rene_branch_name.setVisibility(View.VISIBLE);
		    		tv_edit_rene_micr.setVisibility(View.GONE);
		    		
		    		et_edit_rene_bank_name.setVisibility(View.VISIBLE);
		    		et_edit_rene_branch_name.setVisibility(View.VISIBLE);
		    		et_edit_rene_micr_code.setVisibility(View.GONE);
		    		
		    		et_edit_rene_bank_name.setText(i.getStringExtra("bank_name"));
		    		et_edit_rene_branch_name.setText(i.getStringExtra("branch_name"));
		    	}
		    	else{
		    		tv_edit_rene_bank_name.setVisibility(View.GONE);
		    		tv_edit_rene_branch_name.setVisibility(View.GONE);
		    		tv_edit_rene_micr.setVisibility(View.VISIBLE);
		    		
		    		et_edit_rene_bank_name.setVisibility(View.GONE);
		    		et_edit_rene_branch_name.setVisibility(View.GONE);
		    		et_edit_rene_micr_code.setVisibility(View.VISIBLE);
		    		
		    		et_edit_rene_micr_code.setText(i.getStringExtra("micr"));
		    	}
		    }   	
	    	
	    	et_edit_rene_policy_no.setText(policy_no);
	    	et_edit_rene_accnt_no.setText(i.getStringExtra("accnt_no"));
	    	
	    	et_edit_rene_cheque_no.setText(i.getStringExtra("cheque_no"));
	    	et_edit_rene_cheque_date.setText(i.getStringExtra("cheque_date"));
	    	et_edit_rene_cheque_amt.setText(i.getStringExtra("cheque_amount"));
	    	
	    	et_edit_rene_pay_mode.setText(i.getStringExtra("pay_mode"));
	    	et_edit_rene_pay_type.setText(i.getStringExtra("pay_type"));
	    	
	    	et_edit_rene_payment_type.setText(i.getStringExtra("payment_type"));
	    	
	    	et_edit_altr_mob_no.setText(i.getStringExtra("mob_no"));
	    	
	    	//Call DatePicker for Cheque Date(dd/mm/yyyy)
	    	et_edit_rene_cheque_date.setOnFocusChangeListener(new OnFocusChangeListener() {			
	        	
				public void onFocusChange(View v, boolean hasFocus) {
					if(hasFocus){						
				        showDialog(888);
						
					}				
				}
			});
	        
	        //Call DatePicker for Cheque Date(dd/mm/yyyy)
	    	et_edit_rene_cheque_date.setOnClickListener(new OnClickListener() {			
				public void onClick(View v) {
			        showDialog(888);				
				}
			});
	    	
	    	et_edit_altr_mob_no.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				public void onFocusChange(View v, boolean hasFocus) {
					if(!hasFocus){
						mobNoValidation();
					}
									
				}
			});
	    
	}
	
	/*//Camera
	public void rp_nb_camera(View v){
		if(policy_no != null){
			String root_dir = Environment.getExternalStorageDirectory().toString();
			File myDir = new File(root_dir+"/"+projectName+"/"+renewalFolderName);		
					
			if(!myDir.exists()){
				System.out.println("Dir not exist");
				myDir.mkdirs();
			}
									
			//use standard intent to capture an image
			Intent captureIntent = new Intent("android.media.action.IMAGE_CAPTURE");	    
			File temp_file = new File(myDir,policy_no+".png");
			Uri photoPath = Uri.fromFile(temp_file);
			captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoPath);
			        
			startActivityForResult(captureIntent, CAMERA_CAPTURE);
		}
	}
	
	//Gallery
	public void rp_nb_gallery(View v){
		if(policy_no != null){
			Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(i, RESULT_LOAD_IMAGE);
		}
		else{
			Toast.makeText(getApplicationContext(), "Policy number cannot be blank.", Toast.LENGTH_SHORT).show();
		}
	}
	
	//View doc
	public void rp_nb_view(View v){
		//Checking for the existence of the pdf file
		if(fileValidation()){
			String root = Environment.getExternalStorageDirectory().toString();
			File myDir = new File(root+"/"+projectName+"/"+renewalFolderName);
			if(myDir.exists()){
				
				//Show pdf
				File file_pdf = new File(myDir, policy_no+".pdf");
				Uri path = Uri.fromFile(file_pdf);
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(path, "application/pdf");
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				
				try{
					startActivity(intent);
				}
				catch(ActivityNotFoundException e){
					Toast.makeText(getApplicationContext(), "No application available to view the document.", Toast.LENGTH_SHORT).show();
				}				
			}		
		}
	}
	*/
	public void submit(View v){
        String accnt_no = et_edit_rene_accnt_no.getText().toString().trim();
        String cheque_no = et_edit_rene_cheque_no.getText().toString().trim();
        String cheque_date = et_edit_rene_cheque_date.getText().toString().trim();
        String mob_no = et_edit_altr_mob_no.getText().toString().trim();
		
		mobNoValidation();
		
		if(accnt_no.length()!=0 && cheque_no.length()!=0 && cheque_date.length()!=0 && mob_no.length() == 10){
			// if(fileValidation()){
				 Toast.makeText(getApplicationContext(), "Validation succeeded.", Toast.LENGTH_SHORT).show();
				 
				 //Checking for duplicate cheque no
				 if(dbhelper.isDuplicateChequeNo(cheque_no)){
					 RenewalPremiumNBBean bean = new RenewalPremiumNBBean();
					 bean.setRp_rp_nb_id(id);
					 bean.setRp_nb_accnt_no(accnt_no);
					 bean.setRp_nb_cheque_no(cheque_no);
					 bean.setRp_nb_cheque_date(cheque_date);
					 bean.setRp_nb_cust_mob(mob_no);
					 
					 DatabaseHelper db = new DatabaseHelper(getApplicationContext());
					 int isUpdated = db.updateRenewalPremiumNB(bean);
					 
					 if(isUpdated > 0){
						 Toast.makeText(getApplicationContext(), "Details has been updated.", Toast.LENGTH_SHORT).show();
						 Intent i = new Intent(getApplicationContext(), RenewalPremium.class);
						 i.putExtra("edit", true);
						 startActivity(i);
						 finish();
					 }
					 else{
						 Toast.makeText(getApplicationContext(), "Problem occured while updating.", Toast.LENGTH_SHORT).show();
					 }
				 }
				 
				 else{
					 Toast.makeText(getApplicationContext(),
								"Entered cheque number already exist. Please check again.",
								Toast.LENGTH_SHORT).show();
				 }			 
			 //}
		}
		else{
			Toast.makeText(getApplicationContext(), "Please fill up all required fields.", Toast.LENGTH_SHORT).show();
		}
	}
	
	public boolean fileValidation(){	
		if(policy_no != null){
			//Checking for pdf file
			File file_pdf = mStorageUtils.createFileToAppSpecificDir(context, policy_no+".pdf");
			if(file_pdf.exists()){
				return true;
			}
			else{
				Toast.makeText(getApplicationContext(), "Please take document image.", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		else{
			Toast.makeText(getApplicationContext(), "Policy number cannot be blank.", Toast.LENGTH_SHORT).show();
			return false;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == RESULT_OK){
            int CAMERA_CAPTURE = 101;
            int RESULT_LOAD_IMAGE = 100;
            if(requestCode == RESULT_LOAD_IMAGE){
				System.out.println("Gallery image succeeded");
				 
				Uri selectedImage = data.getData();
		        String[] filePathColumn = {MediaColumns.DATA};

		        Cursor cursor = getApplicationContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
		        cursor.moveToFirst();

		        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		        String filePath = cursor.getString(columnIndex);
		        cursor.close();
		        
		        BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 4;

		        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
		        
		        Matrix matrix = new Matrix();
				matrix.postRotate(getImageOrientation(filePath));
                Bitmap gallery_rotated_bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		        
		        storeDocument(gallery_rotated_bitmap, policy_no);

			}
			else if(requestCode == CAMERA_CAPTURE){
				
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 4;
				
				Bitmap bitmap = BitmapFactory.decodeFile(mStorageUtils.createFileToAppSpecificDir(
						context, policy_no+".png").getAbsolutePath(), options);
				
				Matrix matrix = new Matrix();
				matrix.postRotate(getImageOrientation(mStorageUtils.createFileToAppSpecificDir(
						context, policy_no+".png").getAbsolutePath()));
                Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				
				storeDocument(rotatedBitmap, policy_no);
			}
		}
	}
	
	//Store Document
	private void storeDocument(Bitmap bitmap, String fileName){
			
			//Checking for png file
			File file_png = mStorageUtils.createFileToAppSpecificDir(
					context, fileName+".png");
			if(file_png.exists())
				file_png.delete();
			
			File file = mStorageUtils.createFileToAppSpecificDir(
					context, fileName+".pdf");
			if(file.exists()){
				file.delete();
			}
			
			try{
				Document document = new Document(PageSize.A4, 50, 50, 50, 50);
				PdfWriter.getInstance(document, new FileOutputStream(file));
				document.open();
				
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);			
				Image image = Image.getInstance(outputStream.toByteArray());
				
				//Checking Bitmap and document width and height
				Rectangle rectangle = document.getPageSize();
				if(image.getWidth() > rectangle.getWidth() || image.getWidth() > rectangle.getWidth()){
					//Bitmap size is greater than document size
					image.scaleAbsolute(rectangle.getWidth(), rectangle.getHeight());
				}
				
				//Setting image to center of the document
				image.setAbsolutePosition((rectangle.getWidth() - image.getScaledWidth())/2, (rectangle.getHeight() - image.getScaledHeight())/2);
				
				image.setAlignment(Image.MIDDLE);
				image.scaleToFit(550, 400);
				document.add(image);
				document.close();
				
				outputStream.flush();
				outputStream.close();
				
				Toast.makeText(getApplicationContext(), "Image saved successfully!!", Toast.LENGTH_SHORT).show();
			}
			catch(Exception e){
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "Problem found while saving image.", Toast.LENGTH_SHORT).show();
			}
		}
	
	//Rotate Image
		private int getImageOrientation(String imagePath){
			int rotate = 0;
			
			try{
				File imageFile = new File(imagePath);
				ExifInterface exif = new ExifInterface(imageFile.getPath());
				int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
				
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
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			return rotate;
		}
		
		@Override
		protected Dialog onCreateDialog(int id) {
			if (id == 888) {			
				final Calendar c = Calendar.getInstance();
		        curr_year = c.get(Calendar.YEAR);
		        curr_month = c.get(Calendar.MONTH);
		        curr_day = c.get(Calendar.DAY_OF_MONTH);
		        
		        // Create a new instance of DatePickerDialog and return it
		        return new DatePickerDialog(this, R.style.AppBaseTheme, cheque_date_listener, curr_year, curr_month, curr_day);
			}
			return null;
		}
		
		private DatePickerDialog.OnDateSetListener cheque_date_listener = new DatePickerDialog.OnDateSetListener() {
			
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				
				final Calendar c = Calendar.getInstance();
				
				Calendar selectedDate = new GregorianCalendar(year,monthOfYear,dayOfMonth);
				selectedDate.add(Calendar.HOUR_OF_DAY, c.getTime().getHours());
				selectedDate.add(Calendar.MINUTE, c.getTime().getMinutes());
				selectedDate.add(Calendar.SECOND, c.getTime().getSeconds());
				
		        Calendar minRequiredChequeDate = new GregorianCalendar();
		        minRequiredChequeDate.add(Calendar.MONTH, -3);
		        minRequiredChequeDate.set(Calendar.HOUR_OF_DAY, 0);
		        minRequiredChequeDate.set(Calendar.MINUTE, 0);
		        minRequiredChequeDate.set(Calendar.SECOND, 1);
		       
		        if (selectedDate.before(minRequiredChequeDate)){
		        	Toast.makeText(getApplicationContext(), "Cheque date should not be less than 3 months.", Toast.LENGTH_SHORT).show();
		        	et_edit_rene_cheque_date.setText("");
		        }
		        else if(selectedDate.after(getCurrentDate(c))){
		        	Toast.makeText(getApplicationContext(), "Cheque date should not be future date.", Toast.LENGTH_SHORT).show();
		        	et_edit_rene_cheque_date.setText("");
		        }	
		        else{
		        	//Setting selected cheque date
		        	curr_day = dayOfMonth;
		        	curr_month = monthOfYear;
		        	curr_year = year;		        		
		        	
		        	//Toast.makeText(getApplicationContext(), "Proper cheque date.", Toast.LENGTH_SHORT).show();
		        	showDOBDate(et_edit_rene_cheque_date, year, monthOfYear+1, dayOfMonth);	        	
		        }
			}
		};
		
		//Mobile number validation
		private void mobNoValidation(){
			if(et_edit_altr_mob_no.getText().toString().length() < 10){
				et_edit_altr_mob_no.setError("Mobile number require 10 digits.");
			}
			else{
				et_edit_altr_mob_no.setError(null);
			}
		}
		

		   private void showDOBDate(EditText et, int year, int month, int day) {
//			   et.setText(new StringBuilder().append(day).append("/")
//		      .append(month).append("/").append(year));
			   et.setText(new StringBuilder().append((day<10?"0"+(day):day)).append("/").append((month<10?"0"+(month):month))
						.append("/").append(year));
		   }
		   
		   //Get current date
		   private Calendar getCurrentDate(Calendar c){
		       curr_year = c.get(Calendar.YEAR);
		       curr_month = c.get(Calendar.MONTH);
		       curr_day = c.get(Calendar.DAY_OF_MONTH);
		        
		       Calendar currentDate = new GregorianCalendar(curr_year, curr_month, curr_day);
		       currentDate.add(Calendar.HOUR_OF_DAY, c.getTime().getHours());
		       currentDate.add(Calendar.MINUTE, c.getTime().getMinutes());
		       currentDate.add(Calendar.SECOND, c.getTime().getSeconds());
		       
		       return currentDate;
		   }
			
			public String GetUserType(){
				String  strUserType = "";
					try {
						strUserType = SimpleCrypto.decrypt("SBIL", dbhelper.GetUserType());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}			        	

				return strUserType;
			}
			
			
			
			
			//after 5 minute it will get log out
		   /* private CountDownTimer mCountDown = new CountDownTimer(300000, 300000)
		    	{

		    	    @Override
		    	    public void onTick(long millisUntilFinished)
		    	    {

		    	    }


		    	    @Override
		    	    public void onFinish()
		    	    {
		    	        //show your dialog here
		    	    	new CommonMethods().logoutToLoginActivity(context);
		    	    }
		    	};  


		    	@Override
		    	public void onResume()
		    	{
		    	    super.onResume();

		    	    mCountDown.start();
		    	}  
		    	@Override
		    	public void onPause()
		    	{
		    	    super.onPause();

		    	    mCountDown.cancel();
		    	}  
		    	@Override
		    	public void onUserInteraction()
		    	{
		    	    super.onUserInteraction();

		    	    // user interact cancel the timer and restart to countdown to next interaction
		    	    mCountDown.cancel();
		    	    mCountDown.start();
		    	} */

}
