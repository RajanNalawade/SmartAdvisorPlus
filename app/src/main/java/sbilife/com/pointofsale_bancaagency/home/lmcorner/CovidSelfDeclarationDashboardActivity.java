package sbilife.com.pointofsale_bancaagency.home.lmcorner;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class CovidSelfDeclarationDashboardActivity extends AppCompatActivity {

    private CommonMethods commonMethods;
    private Context context;
    private RecyclerView recyclerview;
    private SelectedAdapter selectedAdapter;
    private String strCIFBDMUserId = "";
    private ArrayList<DBCovidSelfDeclarationValuesModel> globalDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_covid_self_declaration_dashboard);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        commonMethods = new CommonMethods();
        context = this;
        commonMethods.setApplicationToolbarMenu(this, "Covid Self Declaration Dashboard");
        getUserDetails();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        globalDataList = new ArrayList<>();
        globalDataList = databaseHelper.getCovidSelfDeclarationByIACode(strCIFBDMUserId);

        if (globalDataList.size() > 0) {
            recyclerview = findViewById(R.id.recyclerview);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
            // set LayoutManager to RecyclerView
            recyclerview.setLayoutManager(linearLayoutManager);
            // call the constructor of CustomAdapter to send the reference and data to Adapter

            selectedAdapter = new SelectedAdapter(globalDataList);
            recyclerview.setAdapter(selectedAdapter);
            recyclerview.setItemAnimator(new DefaultItemAnimator());
        }
    }

    private void getUserDetails() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = commonMethods.setUserDetails(context);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
    }

    public class SelectedAdapter extends RecyclerView.Adapter<SelectedAdapter.
            ViewHolderAdapter> {

        private final ArrayList<DBCovidSelfDeclarationValuesModel> lstAdapterList;

        SelectedAdapter(ArrayList<DBCovidSelfDeclarationValuesModel> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
        }


        @Override
        public int getItemCount() {
            return lstAdapterList.size() + 1;
        }

        @Override
        public ViewHolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_item_covid_self_dashboard, parent, false);
            return new ViewHolderAdapter(mView);
        }


        @Override
        public void onBindViewHolder(final ViewHolderAdapter holder, int position) {

            int rowPos = holder.getAdapterPosition();

            if (rowPos == 0) {
                // Header Cells. Main Headings appear here
                holder.tvIACode.setBackgroundResource(R.drawable.table_header_cell_bg);
                holder.tvDate.setBackgroundResource(R.drawable.table_header_cell_bg);
                holder.tvStatus.setBackgroundResource(R.drawable.table_header_cell_bg);

                holder.tvIACode.setTextColor(Color.WHITE);
                holder.tvDate.setTextColor(Color.WHITE);
                holder.tvStatus.setTextColor(Color.WHITE);

                holder.tvIACode.setText("IA Code");
                holder.tvDate.setText("Date & Time");
                holder.tvStatus.setText("Status");
            } else {
                DBCovidSelfDeclarationValuesModel modal = lstAdapterList.get(rowPos - 1);

                // Content Cells. Content appear here
                holder.tvIACode.setBackgroundResource(R.drawable.table_content_cell_bg);
                holder.tvDate.setBackgroundResource(R.drawable.table_content_cell_bg);
                holder.tvStatus.setBackgroundResource(R.drawable.table_content_cell_bg);

                holder.tvIACode.setText(modal.getIACode());
                holder.tvDate.setText(modal.getDate() + " " + modal.getTime());
                ParseXML parseXML = new ParseXML();
                String greenRedTag = parseXML.parseXmlTag(modal.getXmlString(), "GreenRedFlag");

                if (greenRedTag.equalsIgnoreCase("Yes")) {
                    holder.tvStatus.setText("Green");
                    holder.tvStatus.setBackgroundColor(Color.parseColor("#008000"));
                } else {
                    holder.tvStatus.setText("Red");
                    holder.tvStatus.setBackgroundColor(Color.parseColor("#FF0000"));
                }
                holder.tvStatus.setTextColor(Color.parseColor("#FFFFFF"));
            }

        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener,
                MenuItem.OnMenuItemClickListener {

            private final TextView tvIACode, tvDate, tvStatus;

            ViewHolderAdapter(View v) {
                super(v);
                tvIACode = v.findViewById(R.id.tvIACode);
                tvDate = v.findViewById(R.id.tvDate);
                tvStatus = v.findViewById(R.id.tvStatus);
                v.setOnCreateContextMenuListener(this);
            }


            @Override
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {

                menu.setHeaderTitle("Select Action");
                int index = getAdapterPosition();
                if (index != 0) {
                    MenuItem viewSelfDeclaration = menu.add(Menu.NONE, 1, 1, "View Self Declaration");
                    viewSelfDeclaration.setOnMenuItemClickListener(this);
                }

            }

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case 1:
                        int index = getAdapterPosition();
                        if (index != 0) {
                            Intent intent = new Intent(context, ViewCovidSelfDeclarationActivity.class);
                            intent.putExtra("xmlString", globalDataList.get(index - 1).getXmlString());
                            startActivity(intent);
                        }
                        break;
                }
                return true;
            }

        }


    }

    public class DBCovidSelfDeclarationValuesModel {
        private String IACode, date, xmlString, time;

        public DBCovidSelfDeclarationValuesModel(String IACode, String date, String xmlString, String time) {
            this.IACode = IACode;
            this.date = date;
            this.xmlString = xmlString;
            this.time = time;
        }

        public String getIACode() {
            return IACode;
        }

        public String getDate() {
            return date;
        }

        public String getXmlString() {
            return xmlString;
        }

        public String getTime() {
            return time;
        }
    }
}