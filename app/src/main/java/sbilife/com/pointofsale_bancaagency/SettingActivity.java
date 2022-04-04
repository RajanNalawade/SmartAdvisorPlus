package sbilife.com.pointofsale_bancaagency;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class SettingActivity extends AppCompatActivity {
	
	/*
	 * these are all global variables.
	 */
	
	

	private DatabaseHelper dbhelper;
	private String strCIFNo;
	//private String strPassword;
	
	protected	ListView lv;	
	 
	private Context context;
	private boolean flagexpForgetCIFNo = true;
	private boolean flagexpForgetPassword = true;
	
	private EditText edQuestion;

    private EditText edQuestion_Password;
	//private TextView txtQuestion_Password;
	
	private EditText edNew_Password,edConfirm_New_Password;


    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.setting);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        
        dbhelper = new DatabaseHelper(this);
		context = this;
        new CommonMethods().setApplicationToolbarMenu(this, "Settings");
        final LinearLayout lnForgetCIFNo = findViewById(R.id.lnForgetCIFNo);
        final LinearLayout lnForgetPassword = findViewById(R.id.lnForgetPassword);
        
        TextView expForgetCIFNo = findViewById(R.id.expForgetCIFNo);
        TextView expForgetPassword = findViewById(R.id.expForgetPassword);
        
        
       // final ImageButton imgexpimageIntroduction_button = (ImageButton)findViewById(R.id.expimageforgetcif_button);
       // final ImageButton imgexpimagePassord_button = (ImageButton)findViewById(R.id.expimageforgetpassword_button);
        
        String UserType = new CommonMethods().GetUserType(context);
		
		if(UserType.contentEquals("MAN") || UserType.contentEquals("BDM") || UserType.contentEquals("CIF"))
		{
			expForgetCIFNo.setText("Forget CIF Code/ User Id/ BDM Code");
		}
		else
		{
			expForgetCIFNo.setText("Forget IA Code");
		}
                       
        edQuestion = findViewById(R.id.edQuestion);
        TextView txtQuestion = findViewById(R.id.txtQuestion);
        
        edQuestion_Password = findViewById(R.id.edQuestion_Password);
      //  txtQuestion_Password = (TextView)findViewById(R.id.txtQuestion_Password);
        
        edNew_Password = findViewById(R.id.edNew_Password);
        edConfirm_New_Password = findViewById(R.id.edConfirm_New_Password);
        
        edNew_Password.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

		    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
		        return false;
		    }

		    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
		        return false;
		    }

		    public boolean onActionItemClicked(ActionMode actionMode, MenuItem item) {
		        return false;
		    }

		    public void onDestroyActionMode(ActionMode actionMode) {
		    }
		});

        edNew_Password.setLongClickable(false);
        edNew_Password.setTextIsSelectable(false);
        
        edConfirm_New_Password.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

		    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
		        return false;
		    }

		    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
		        return false;
		    }

		    public boolean onActionItemClicked(ActionMode actionMode, MenuItem item) {
		        return false;
		    }

		    public void onDestroyActionMode(ActionMode actionMode) {
		    }
		});

        edConfirm_New_Password.setLongClickable(false);
        edConfirm_New_Password.setTextIsSelectable(false);
        
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter(){	            
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (end > start) {

                    char[] acceptedChars = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 
                            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '@', '.', '_', '#', '$', '%', '&', '*', '-', '+', '(', ')', '!', '"', '\'', ':', 
                             '/', '?', ',', '~', '`', '|', '\\', '^', '<', '>', '{', '}', '[', ']', '=','.'};

                    for (int index = start; index < end; index++) {                                         
                        if (!new String(acceptedChars).contains(String.valueOf(source.charAt(index)))) { 
                            return ""; 
                        }               
                    }
                }
                return null;
            }

        };
        edQuestion_Password.setFilters(filters);
        edNew_Password.setFilters(filters);
        edConfirm_New_Password.setFilters(filters);


        String strQue = dbhelper.GetQuestion();
        try {
			strQue = SimpleCrypto.decrypt("SBIL", strQue);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        txtQuestion.setText(strQue);
        //txtQuestion_Password.setText(strQue);
       
        OnTouchListener layout1Listener = new OnTouchListener() {
            public boolean  onTouch  (View  v, MotionEvent  event) {
                if (event.getAction()==MotionEvent.ACTION_UP){

					if (flagexpForgetCIFNo) {
						// imgexpimageIntroduction_button.setImageResource(R.drawable.up);
						lnForgetCIFNo.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
						lnForgetCIFNo.requestLayout();
						flagexpForgetCIFNo = false;

					} else {
						// imgexpimageIntroduction_button.setImageResource(R.drawable.down);
						lnForgetCIFNo.getLayoutParams().height = 0;
						lnForgetCIFNo.requestLayout();
						flagexpForgetCIFNo = true;
					}
				}             
                return false;
            }
        };  
        
        /*OnTouchListener ImageButton1Listener = new OnTouchListener() {
            public boolean  onTouch  (View  v, MotionEvent  event) {
                if (event.getAction()==MotionEvent.ACTION_UP){

                	if(flagexpForgetCIFNo)
                	{
                	
                	//imgexpimageIntroduction_button.setImageResource(R.drawable.up);
                	lnForgetCIFNo.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
                	lnForgetCIFNo.requestLayout();  
                	
                	flagexpForgetCIFNo = false;
    				
                	}
                	else
                	{
                		//imgexpimageIntroduction_button.setImageResource(R.drawable.down);
                		lnForgetCIFNo.getLayoutParams().height = 0;
                		lnForgetCIFNo.requestLayout();
        				flagexpForgetCIFNo = true;
                	}               	
                }                
                return false;
            }
        };  */
        
        
        OnTouchListener layoutPassword1Listener = new OnTouchListener() {
            public boolean  onTouch  (View  v, MotionEvent  event) {
                if (event.getAction()==MotionEvent.ACTION_UP){

                	if(flagexpForgetPassword)
                	{
                	
                	//imgexpimagePassord_button.setImageResource(R.drawable.up);
                	lnForgetPassword.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
                	lnForgetPassword.requestLayout();  
                	
                	flagexpForgetPassword = false;
    				
                	}
                	else
                	{
                		//imgexpimagePassord_button.setImageResource(R.drawable.down);
                		lnForgetPassword.getLayoutParams().height = 0;
                		lnForgetPassword.requestLayout();
        				flagexpForgetPassword = true;
                	}               	
                }                
                return false;
            }
        };  
        
        /*OnTouchListener ImageButtonPassword1Listener = new OnTouchListener() {
            public boolean  onTouch  (View  v, MotionEvent  event) {
                if (event.getAction()==MotionEvent.ACTION_UP){

                	if(flagexpForgetPassword)
                	{
                	
                	//imgexpimagePassord_button.setImageResource(R.drawable.up);
                	lnForgetPassword.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
                	lnForgetPassword.requestLayout();  
                	
                	flagexpForgetPassword = false;
    				
                	}
                	else
                	{
                		//imgexpimagePassord_button.setImageResource(R.drawable.down);
                		lnForgetPassword.getLayoutParams().height = 0;
                		lnForgetPassword.requestLayout();
        				flagexpForgetPassword = true;
                	}               	
                }                
                return false;
            }
        }; */ 
        
        expForgetCIFNo.setOnTouchListener(layout1Listener);
        
       // imgexpimageIntroduction_button.setOnTouchListener(ImageButton1Listener);
        
        expForgetPassword.setOnTouchListener(layoutPassword1Listener);
        
       // imgexpimagePassord_button.setOnTouchListener(ImageButtonPassword1Listener);
	}	
	
	/*
	 * based on security answer it will retrieve BDM/CIF code
	 */
	
	public void btn_save(View v)
	{
		 String strpatename = edQuestion.getText().toString();
		 
		 if (strpatename.equalsIgnoreCase(""))
		 {			
			 final Dialog dialog = new Dialog(this);
		    	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
		    	dialog.setContentView(R.layout.loading_window);		
		    	TextView text = dialog.findViewById(R.id.txtalertheader);
		    	text.setText("Please Enter Answer..");		
		    	Button dialogButton = dialog.findViewById(R.id.btnalert);
		    	dialogButton.setOnClickListener(new OnClickListener() {			
		    		public void onClick(View v) {
		    			dialog.dismiss();    			
		    		}
		    	});

		    	dialog.show();			
		 } 
		 else
		 {
			 if(strpatename.length() > 30)
			 {
				 Alert30Digit("Security Answer");
			 }
			 else
			 {	
				 
			 strpatename = strpatename.toLowerCase();
			 try {
				strCIFNo = dbhelper.getCIFNo(SimpleCrypto.encrypt("SBIL",strpatename));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		if(strCIFNo == "" || strCIFNo == null)
		{
			final Dialog dialog = new Dialog(this);
	    	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
	    	dialog.setContentView(R.layout.loading_window);		
	    	TextView text = dialog.findViewById(R.id.txtalertheader);
	    	text.setText("Please Enter Correct Answer..");		
	    	Button dialogButton = dialog.findViewById(R.id.btnalert);
	    	dialogButton.setOnClickListener(new OnClickListener() {			
	    		public void onClick(View v) {
	    			dialog.dismiss();    			
	    		}
	    	});

	    	dialog.show();
		}
		else
		{
			final Dialog dialog = new Dialog(this);
	    	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
	    	dialog.setContentView(R.layout.loading_window);		
	    	TextView text = dialog.findViewById(R.id.txtalertheader);
	    	try {
	    		
	    		String UserType = new CommonMethods().GetUserType(context);
	    		
	    		if(UserType.contentEquals("MAN") || UserType.contentEquals("BDM") || UserType.contentEquals("CIF") || UserType.contentEquals("UM"))
	    		{
	    			text.setText("Your CIF Code/ User Id/ BDM Code is : " +SimpleCrypto.decrypt("SBIL",strCIFNo));
	    		}
	    		else
	    		{
	    			text.setText("Your IA Code is : " +SimpleCrypto.decrypt("SBIL",strCIFNo));
	    		}
	    						
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
	    	Button dialogButton = dialog.findViewById(R.id.btnalert);
	    	dialogButton.setOnClickListener(new OnClickListener() {			
	    		public void onClick(View v) {
	    			dialog.dismiss();    			
	    		}
	    	});

	    	dialog.show();
		}
		 }
		 }
		
	}
	public void btn_cancel(View v)
	{
		finish();
	}
	
	/*
	 * it will change your current password
	 */
	
	public void btn_save_pass(View v)
	{
		 String strpatename = edQuestion_Password.getText().toString();
		 String strnewpassword = edNew_Password.getText().toString();
		 String strconfirmnewpass = edConfirm_New_Password.getText().toString();
		 
		 if (strpatename.equalsIgnoreCase("")|| strnewpassword.equalsIgnoreCase("") || strconfirmnewpass.equalsIgnoreCase(""))
		 {			
			 final Dialog dialog = new Dialog(this);
		    	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
		    	dialog.setContentView(R.layout.loading_window);		
		    	TextView text = dialog.findViewById(R.id.txtalertheader);
		    	text.setText("All Fields Required..");		
		    	Button dialogButton = dialog.findViewById(R.id.btnalert);
		    	dialogButton.setOnClickListener(new OnClickListener() {			
		    		public void onClick(View v) {
		    			dialog.dismiss();    			
		    		}
		    	});

		    	dialog.show();			
		 } 
		 else
		 {
			 if(strpatename.length() > 20)
			 {
				 Alert20Digit("Old Password");
			 }
			 else if(strnewpassword.length() > 20)
			 {
				 Alert20Digit("New Password");
			 }
			 else if(strconfirmnewpass.length() > 20)
			 {
				 Alert20Digit("Confirm New Password");
			 }
			 else
			 {				 		
			 //strpatename = strpatename.toLowerCase();
			 //strPassword = dbhelper.getpassword(strpatename);
		
			 if(!strnewpassword.contentEquals(strconfirmnewpass))
				{
					passAlert();
				}
				else
				{
					
					String strOldPassword = null;
					try {
						strOldPassword = dbhelper.VarifyOldPassword(SimpleCrypto.encrypt("SBIL",strpatename));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(strOldPassword != "")
					{
					
					ArrayList<String> lstevent = new ArrayList<String>();
					
					lstevent.clear();
					
					Cursor c = dbhelper.GetProfile();
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
							lstevent.add(c.getString(c.getColumnIndex("LoginType")));
							lstevent.add(c.getString(c.getColumnIndex("LoginID")));							
							c.moveToNext();
						}
					}			        					
					
					clsLogin objcla = null;
					try {
						objcla = new clsLogin(lstevent.get(0),
                                lstevent.get(1),
                                lstevent.get(2),
                                lstevent.get(3),
                                lstevent.get(4),
                                lstevent.get(5),
                                lstevent.get(6),
                                lstevent.get(7),
						SimpleCrypto.encrypt("SBIL",strnewpassword),
						SimpleCrypto.encrypt("SBIL",strconfirmnewpass),
                                lstevent.get(10),
                                lstevent.get(11),
                                lstevent.get(12),
                                lstevent.get(13),
						SimpleCrypto.encrypt("SBIL",""));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					dbhelper.UpdateRecord(objcla, lstevent.get(14));
					
			final Dialog dialog = new Dialog(this);
	    	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
	    	dialog.setContentView(R.layout.loading_window);		
	    	TextView text = dialog.findViewById(R.id.txtalertheader);
	    	text.setText("Password Successfully Changed..");		
	    	Button dialogButton = dialog.findViewById(R.id.btnalert);
	    	dialogButton.setOnClickListener(new OnClickListener() {			
	    		public void onClick(View v) {
	    			dialog.dismiss();    			
	    		}
	    	});

	    	dialog.show();
		}
					else
					{
						final Dialog dialog = new Dialog(this);
				    	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
				    	dialog.setContentView(R.layout.loading_window);		
				    	TextView text = dialog.findViewById(R.id.txtalertheader);
				    	text.setText("Old Password do not match..");		
				    	Button dialogButton = dialog.findViewById(R.id.btnalert);
				    	dialogButton.setOnClickListener(new OnClickListener() {			
				    		public void onClick(View v) {
				    			dialog.dismiss();    			
				    		}
				    	});

				    	dialog.show();
					}
				}
		 }
		 }
		
	}
	public void btn_cancel_pass(View v)
	{
		finish();
	}
	

	/*
	 * when old password and confirm password not same that time this alert shows
	 */
	
	private void passAlert()
	{	
		
		final Dialog dialog = new Dialog(this);		
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 	
		dialog.setContentView(R.layout.loading_window);		
		TextView text = dialog.findViewById(R.id.txtalertheader);
		text.setText("Confirm Password Not Match..");		
		Button dialogButton = dialog.findViewById(R.id.btnalert);
		dialogButton.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}
	
	/*
	 * character validation alert
	 */
	
	private void Alert20Digit(String str)
	{	
		
		final Dialog dialog = new Dialog(this);		
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 	
		dialog.setContentView(R.layout.loading_window);		
		TextView text = dialog.findViewById(R.id.txtalertheader);
		text.setText("You can  not enter more than 20 Character " + str);		
		Button dialogButton = dialog.findViewById(R.id.btnalert);
		dialogButton.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}
	
	/*
	 * character validation alert
	 */
	
	private void Alert30Digit(String str)
	{	
		
		final Dialog dialog = new Dialog(this);		
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 	
		dialog.setContentView(R.layout.loading_window);		
		TextView text = dialog.findViewById(R.id.txtalertheader);
		text.setText("You can  not enter more than 30 Character " + str);		
		Button dialogButton = dialog.findViewById(R.id.btnalert);
		dialogButton.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
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
    	
    	@Override
    	public void onBackPressed() {
    		// TODO Auto-generated method stub
    		super.onBackPressed();
    	}
}