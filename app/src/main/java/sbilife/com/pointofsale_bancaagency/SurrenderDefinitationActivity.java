package sbilife.com.pointofsale_bancaagency;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class SurrenderDefinitationActivity extends AppCompatActivity {
	
	/*
	 * these are all global variables.
	 */		
	
	private DatabaseHelper db;
	private final Context context = this;
		
	protected	ListView lv;	
	 		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		
        setContentView(R.layout.surrenderdef);
        
        new CommonMethods().setActionbarLayout(this);
        
       db = new DatabaseHelper(context);
        /*
    	 * based on product name it will show video
    	 */
        
        Intent intent = getIntent();       
        String strDef = intent.getStringExtra("SurDef");
        
        TextView txtdef = findViewById(R.id.txtdef);
        txtdef.setText(strDef);
        
	}	

}