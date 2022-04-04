package sbilife.com.pointofsale_bancaagency.smartmoneybackgold;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;


public class Success extends AppCompatActivity {


    private String pdf;
    private String sumAssured;
    private String gender;
    private String age;
    private String policyTerm;
    private String premFreq;
    String ppt;
    private String isStaff;
    private String plan;
    private String PTAflag;
    private String ADBflag;
    private String ATPDflag;
    private String CC13flag;
    private String PTAterm;
    private String ADBfterm;
    private String ATPDterm;
    private String CC13term;
    private String PTAsa;
    private String ADBfsa;
    private String ATPDsa;
    private String CC13sa;
    private Button back;
    private Button gohome;
    Button viewPdf;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smartmoneysuccess);
        new CommonMethods().setActionbarLayout(this);


        Intent i = getIntent();
        // Receiving the Data
        String op = i.getStringExtra("op");
        String op1 = i.getStringExtra("op1");
        String op2 = i.getStringExtra("op2");
        String op3 = i.getStringExtra("op3");
        String op4 = i.getStringExtra("op4");
        String op5 = i.getStringExtra("op5");
        String op6 = i.getStringExtra("op6");
        String op7 = i.getStringExtra("op7");
        String op8 = i.getStringExtra("op8");
        String op9 = i.getStringExtra("op9");
        String op10 = i.getStringExtra("op10");

        pdf = i.getStringExtra("pdf");

        age = i.getStringExtra("age");
        gender = i.getStringExtra("gender");
        sumAssured = i.getStringExtra("sumAssured");
        plan = i.getStringExtra("plan");
        premFreq = i.getStringExtra("premFreq");
        policyTerm = i.getStringExtra("policyTerm");
        isStaff = i.getStringExtra("isStaff");

        PTAflag = i.getStringExtra("PTAflag");
        ADBflag = i.getStringExtra("ADBflag");
        ATPDflag = i.getStringExtra("ATPDflag");
        CC13flag = i.getStringExtra("CC13flag");

        if (PTAflag.equalsIgnoreCase("true")) {
            PTAterm = i.getStringExtra("PTATerm");
            PTAsa = i.getStringExtra("PTASumAssured");
        }
        if (ADBflag.equalsIgnoreCase("true")) {
            ADBfterm = i.getStringExtra("ADBTerm");
            ADBfsa = i.getStringExtra("ADBSumAssured");
        }
        if (ATPDflag.equalsIgnoreCase("true")) {
            ATPDterm = i.getStringExtra("ATPDTerm");
            ATPDsa = i.getStringExtra("ATPDSumAssured");
        }
        if (CC13flag.equalsIgnoreCase("true")) {
            CC13term = i.getStringExtra("CC13Term");
            CC13sa = i.getStringExtra("CC13SumAssured");
        }


        //Declaration
        TextView output = findViewById(R.id.output);
        TextView output1 = findViewById(R.id.output1);
        TextView output2 = findViewById(R.id.output2);
        TextView output3 = findViewById(R.id.output3);
        TextView output4 = findViewById(R.id.output4);
        TextView output5 = findViewById(R.id.output5);
        TextView output6 = findViewById(R.id.output6);
        TextView output7 = findViewById(R.id.output7);
        TextView output8 = findViewById(R.id.output8);
        TextView output9 = findViewById(R.id.output9);
        TextView output10 = findViewById(R.id.output10);


        //Display output
        output.setText(op);
        output1.setText(op1);
        output2.setText(op2);
        output3.setText(op3);
        output4.setText(op4);
        output5.setText(op5);
        output6.setText(op6);
        output7.setText(op7);
        output8.setText(op8);
        output9.setText(op9);
        output10.setText(op10);

        if (op7 == null)
            output7.setVisibility(View.GONE);
        else
            output7.setText(op7);

        if (op8 == null)
            output8.setVisibility(View.GONE);
        else
            output8.setText(op8);

        if (op9 == null)
            output9.setVisibility(View.GONE);
        else
            output9.setText(op9);
        if (op10 == null)
            output10.setVisibility(View.GONE);
        else
            output10.setText(op10);


        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                setResult(RESULT_OK);
                finish();
            }
        });

        gohome = findViewById(R.id.gohome);
		/*gohome.setOnClickListener(new View.OnClickListener() {
		
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), Product.class);
				startActivity(i);
			}
		});	*/

    }
}
