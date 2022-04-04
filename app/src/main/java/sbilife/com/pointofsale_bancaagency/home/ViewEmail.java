package sbilife.com.pointofsale_bancaagency.home;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.SimpleCrypto;

public class ViewEmail extends AppCompatActivity {
    private DatabaseHelper db;

    final Context context = this;
    protected ListView lv;

    private EditText edEmailId;
    private TextView txtemailtype;
    private String strRowID = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.viewemail);
        new CommonMethods().setActionbarLayout(this);

        db = new DatabaseHelper(this);

        edEmailId = findViewById(R.id.edEmailId);
        txtemailtype = findViewById(R.id.txtemailtype);

        Intent i = getIntent();
        String strEmailId = i.getStringExtra("strEmailId");
        //strRowID = i.getStringExtra("strRowId");
        String strNewEmailID = strEmailId.replace("Email Id: ", "").trim();
        String strEmailType = db.getEmailGroup(strNewEmailID);
        strRowID = db.getEmailRowId(strNewEmailID);
        if (strEmailType != "") {
            txtemailtype.setText(strEmailType);
        }

        edEmailId.setText(strNewEmailID);

    }

    public void btn_updateEmail(View v) {
        String strEmailType = txtemailtype.getText().toString();
        String strEmailId = edEmailId.getText().toString();

        if (strEmailId.equalsIgnoreCase("") || strEmailType.equalsIgnoreCase("")) {
            validation();
        } else {
            ArrayList<String> lstevent = new ArrayList<String>();
            lstevent.clear();

            Cursor c = db.GetAllEmail();

            if (c.getCount() > 0) {
                c.moveToFirst();
                for (int ii = 0; ii < c.getCount(); ii++) {
                    lstevent.add(c.getString(c.getColumnIndex("EmailType")));
                    lstevent.add(c.getString(c.getColumnIndex("EmailName")));
                    lstevent.add(c.getString(c.getColumnIndex("EmailCreatedDate")));
                    lstevent.add(c.getString(c.getColumnIndex("EmailCreatedBy")));
                    lstevent.add(c.getString(c.getColumnIndex("EmailModifiedDate")));
                    lstevent.add(c.getString(c.getColumnIndex("EmailModifiedBy")));
                    lstevent.add(c.getString(c.getColumnIndex("EmailUserId")));
                    lstevent.add(c.getString(c.getColumnIndex("EmailID")));
                    c.moveToNext();
                }
            }
			
			
				/*clsEmail objcls = new clsEmail(txtemailtype.getText().toString(),
				edEmailId.getText().toString(),
				lstevent.get(2).toString(),
				lstevent.get(3).toString(),
				lstevent.get(4).toString(),
				lstevent.get(5).toString());			
			
				db.UpdateEmail(objcls,lstevent.get(6).toString());*/

            clsEmail objcls = new clsEmail(txtemailtype.getText().toString(),
                    edEmailId.getText().toString(),
                    "0",
                    "0",
                    "0",
                    "0",
                    lstevent.get(6));

            db.UpdateEmail(objcls, strRowID);

            updatealert();

            finish();
        }

    }

    public void btn_cancelEmail(View v) {
        finish();
    }

    private void updatealert() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("Update Successfully...");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void validation() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_window);
        TextView text = dialog.findViewById(R.id.txtalertheader);
        text.setText("Please Enter Email Id...");
        Button dialogButton = dialog.findViewById(R.id.btnalert);
        dialogButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
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
    }

    public String GetUserType() {
        String strUserType = "";
        try {
            strUserType = SimpleCrypto.decrypt("SBIL", db.GetUserType());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return strUserType;
    }

    //after 5 minute it will get log out
  	/*private CountDownTimer mCountDown = new CountDownTimer(300000, 300000)
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
  		}*/

}
