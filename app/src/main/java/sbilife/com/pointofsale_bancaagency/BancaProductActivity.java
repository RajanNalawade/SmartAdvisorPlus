package sbilife.com.pointofsale_bancaagency;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.home.CarouselHomeActivity;

public class BancaProductActivity extends AppCompatActivity {
    private TableLayout tblIndividual, tblHealth;
    private LinearLayout lnIndividual, lnHealth;

    private Context con;
    //private StateIdAsyncTask stateIdAsyncTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.product);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);


        con = this;

        CommonMethods commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "Products");

        //Added by Tushar Kadam to get Kerala Resident
        /*String kerlaDiscountDetails = AppSharedPreferences.getData(con,(new CommonMethods().getKerlaDiscount()),"");
        if (TextUtils.isEmpty(kerlaDiscountDetails)) {
            stateIdAsyncTask = new StateIdAsyncTask(con, commonMethods.GetUserCode(con), commonMethods.GetUserType(con));
            stateIdAsyncTask.execute();
        }*/
        tblIndividual = findViewById(R.id.tblIndividual);
        tblHealth = findViewById(R.id.tblHealth);

        lnIndividual = findViewById(R.id.lnIndividual);
        lnHealth = findViewById(R.id.lnHealth);

        TextView expIndividual = findViewById(R.id.expIndividual);
        TextView expHealth = findViewById(R.id.expHealth);


        Spinner group = findViewById(R.id.group_pd);

        TableRow tblgroup = findViewById(R.id.tblgroup_pd);

            tblgroup.setVisibility(View.VISIBLE);
        expIndividual.setOnClickListener(new OnClickListener() {

            //@Override
            public void onClick(View arg0) {
                tblIndividual.setBackgroundResource(R.drawable.exp_selected);
                tblHealth.setBackgroundResource(R.drawable.exp_unselected);

                lnIndividual.setVisibility(View.VISIBLE);
                lnHealth.setVisibility(View.GONE);
            }
        });

        expHealth.setOnClickListener(new OnClickListener() {

            //@Override
            public void onClick(View arg0) {

                tblIndividual.setBackgroundResource(R.drawable.exp_unselected);
                tblHealth.setBackgroundResource(R.drawable.exp_selected);

                lnIndividual.setVisibility(View.GONE);
                lnHealth.setVisibility(View.VISIBLE);
            }
        });

        tblIndividual.setOnClickListener(new OnClickListener() {

            //@Override
            public void onClick(View arg0) {
                tblIndividual.setBackgroundResource(R.drawable.exp_selected);
                tblHealth.setBackgroundResource(R.drawable.exp_unselected);

                lnIndividual.setVisibility(View.VISIBLE);
                lnHealth.setVisibility(View.GONE);
            }
        });

        tblHealth.setOnClickListener(new OnClickListener() {

            //@Override
            public void onClick(View arg0) {
                tblIndividual.setBackgroundResource(R.drawable.exp_unselected);
                tblHealth.setBackgroundResource(R.drawable.exp_selected);

                lnIndividual.setVisibility(View.GONE);
                lnHealth.setVisibility(View.VISIBLE);
            }
        });


        Spinner unitLinkedPlan = findViewById(R.id.unitLinkedPlans);
        Spinner childPlan = findViewById(R.id.childPlans);
        Spinner pensionPlan = findViewById(R.id.pensionPlans);
        Spinner protectionPlan = findViewById(R.id.protectionPlans);
        Spinner savingPlan = findViewById(R.id.savingPlans);

        Spinner healthPlans = findViewById(R.id.healthPlans);

        Spinner savingPlansBanca = findViewById(R.id.savingPlansBanca);

        /*if (!GetUserType().contentEquals("AGENT")) {
            savingPlan.setVisibility(View.GONE);
            savingPlansBanca.setVisibility(View.VISIBLE);
        } else {
            savingPlan.setVisibility(View.VISIBLE);
            savingPlansBanca.setVisibility(View.GONE);
        }*/
        savingPlan.setVisibility(View.GONE);
        savingPlansBanca.setVisibility(View.VISIBLE);

        unitLinkedPlan.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (id != 0) {
                    Intent i = new Intent(con, ProductDesc_webview.class);
                    i.putExtra("name", parent.getSelectedItem().toString());
                    parent.setSelection(0);
                    startActivity(i);
                    finish();

                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        childPlan.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (id != 0) {

                    Intent i = new Intent(con, ProductDesc_webview.class);
                    i.putExtra("name", parent.getSelectedItem().toString());
                    parent.setSelection(0);
                    startActivity(i);
                    finish();


                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        pensionPlan.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (id != 0) {

                    Intent i = new Intent(con, ProductDesc_webview.class);
                    i.putExtra("name", parent.getSelectedItem().toString());
                    parent.setSelection(0);
                    startActivity(i);
                    finish();


                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        protectionPlan.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (id != 0) {

                    Intent i = new Intent(con, ProductDesc_webview.class);
                    i.putExtra("name", parent.getSelectedItem().toString());
                    parent.setSelection(0);
                    startActivity(i);
                    finish();


                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        savingPlan.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (id != 0) {

                    Intent i = new Intent(con, ProductDesc_webview.class);
                    i.putExtra("name", parent.getSelectedItem().toString());
                    parent.setSelection(0);
                    startActivity(i);
                    finish();


                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        savingPlansBanca.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (id != 0) {

                    Intent i = new Intent(con, ProductDesc_webview.class);
                    i.putExtra("name", parent.getSelectedItem().toString());
                    parent.setSelection(0);
                    startActivity(i);
                    finish();


                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        healthPlans.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (id != 0) {

                    Intent i = new Intent(con, ProductDesc_webview.class);
                    i.putExtra("name", parent.getSelectedItem().toString());
                    parent.setSelection(0);
                    startActivity(i);
                    finish();

                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //group products
        group.setOnItemSelectedListener(new OnItemSelectedListener() {

            //@Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long id) {

                if (id != 0) {

                    Intent i = new Intent(con, ProductDesc_webview.class);
                    i.putExtra("name", arg0.getSelectedItem().toString());
                    arg0.setSelection(0);
                    startActivity(i);

                    finish();
                }
            }

            //@Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    @Override
    protected void onDestroy() {

        /*if (stateIdAsyncTask != null) {
            stateIdAsyncTask.cancel(true);
        }*/
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(BancaProductActivity.this, CarouselHomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }
}