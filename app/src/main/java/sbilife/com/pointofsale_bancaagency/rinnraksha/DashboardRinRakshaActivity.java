package sbilife.com.pointofsale_bancaagency.rinnraksha;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.R;

public class DashboardRinRakshaActivity extends AppCompatActivity {

    private Context mContext;
    private ListView lv_rinraksha_dashboard;
    private ArrayList<RinRakshaBean> lstRinRakshaBeans = new ArrayList<RinRakshaBean>();
    private DashboardRinRakshaAdapter mDashboardRinRakshaAdapter;
    private DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rinraksha_dashboard);

        mContext = getApplicationContext();
        mDatabaseHelper = new DatabaseHelper(mContext);

        lv_rinraksha_dashboard = findViewById(R.id.lv_rinraksha_dashboard);
        mDashboardRinRakshaAdapter = new DashboardRinRakshaAdapter(mContext, lstRinRakshaBeans);
        lv_rinraksha_dashboard.setAdapter(mDashboardRinRakshaAdapter);
        mDashboardRinRakshaAdapter.notifyDataSetChanged();

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                lstRinRakshaBeans = mDatabaseHelper.getRinRakshaDashboardDetails();
                mDashboardRinRakshaAdapter = new DashboardRinRakshaAdapter(mContext, lstRinRakshaBeans);
                lv_rinraksha_dashboard.setAdapter(mDashboardRinRakshaAdapter);
                mDashboardRinRakshaAdapter.notifyDataSetChanged();
            }
        });

    }


    class DashboardRinRakshaAdapter extends BaseAdapter {

        private ArrayList<RinRakshaBean> lstAdapterRinRakshaBeans;
        private Context mAdapterContext;

        DashboardRinRakshaAdapter(Context mAdapterContext, ArrayList<RinRakshaBean> lstAdapterRinRakshaBeans) {
            this.lstAdapterRinRakshaBeans = lstAdapterRinRakshaBeans;
            this.mAdapterContext = mAdapterContext;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return lstAdapterRinRakshaBeans.size();
        }

        @Override
        public RinRakshaBean getItem(int position) {
            // TODO Auto-generated method stub
            return lstAdapterRinRakshaBeans.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater mInflater = LayoutInflater.from(mAdapterContext);
                convertView = mInflater.inflate(R.layout.row_rinraksha_dashboard,
                        null);
                holder = new ViewHolder();

                holder.tv_rinraksha_quotation = convertView.findViewById(R.id.tv_rinraksha_quotation);
                holder.tv_rinraksha_borro1 = convertView.findViewById(R.id.tv_rinraksha_borro1);
                holder.tv_rinraksha_borro1_urn = convertView.findViewById(R.id.tv_rinraksha_borro1_urn);
                //holder.tv_rinraksha_borro2 = (TextView) convertView.findViewById(R.id.tv_rinraksha_borro2);
                holder.tv_rinraksha_borro2_urn = convertView.findViewById(R.id.tv_rinraksha_borro2_urn);
                //holder.tv_rinraksha_borro3 = (TextView) convertView.findViewById(R.id.tv_rinraksha_borro3);
                holder.tv_rinraksha_borro3_urn = convertView.findViewById(R.id.tv_rinraksha_borro3_urn);

                holder.ll_rinraksha_borro1 = convertView.findViewById(R.id.ll_rinraksha_borro1);
                holder.ll_rinraksha_borro2 = convertView.findViewById(R.id.ll_rinraksha_borro2);
                holder.ll_rinraksha_borro3 = convertView.findViewById(R.id.ll_rinraksha_borro3);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final String quotNo = lstAdapterRinRakshaBeans.get(position).getStrQuotationNo(),
                    urn1 = lstAdapterRinRakshaBeans.get(position).getStrURNBorrower1(),
                    urn2 = lstAdapterRinRakshaBeans.get(position).getStrURNBorrower2(),
                    urn3 = lstAdapterRinRakshaBeans.get(position).getStrURNBorrower3();

            holder.tv_rinraksha_quotation.setText(quotNo);

            if (urn2.equals("") || urn2.length() == 0) {
                holder.ll_rinraksha_borro2.setVisibility(View.GONE);
                holder.ll_rinraksha_borro3.setVisibility(View.GONE);

                //holder.tv_rinraksha_borro1.setText("Primary Borrower : ");
            } else if (urn3.equals("") || urn3.length() == 0) {
                holder.ll_rinraksha_borro3.setVisibility(View.GONE);
            }

            holder.tv_rinraksha_borro1_urn.setText(urn1);
            holder.tv_rinraksha_borro2_urn.setText(urn2);
            holder.tv_rinraksha_borro3_urn.setText(urn3);

            return convertView;
        }

        class ViewHolder {
            private TextView tv_rinraksha_quotation, tv_rinraksha_borro1, tv_rinraksha_borro1_urn,
                    tv_rinraksha_borro2_urn, tv_rinraksha_borro3_urn;
            private LinearLayout ll_rinraksha_borro1, ll_rinraksha_borro2, ll_rinraksha_borro3;
        }

    }

}
