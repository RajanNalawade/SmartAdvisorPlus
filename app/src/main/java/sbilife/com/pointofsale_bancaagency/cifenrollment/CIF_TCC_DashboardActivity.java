package sbilife.com.pointofsale_bancaagency.cifenrollment;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import sbilife.com.pointofsale_bancaagency.DatabaseHelper;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class CIF_TCC_DashboardActivity extends AppCompatActivity {

    private Context mContext;
    private CommonMethods mCommonMethods;

    private ArrayList<TCC_ExamDetails_Activity.TCC_ExamDetails> lst_tcc_exam_details = new ArrayList<>();
    private AdapterTCCDAshboard mAdapterTCCDAshboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_cif_tcc_dashboard);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.cifenrollment_pf_activity_title);

        initialization();
    }

    private void initialization(){

        mContext = this;
        mCommonMethods = new CommonMethods();
        DatabaseHelper db = new DatabaseHelper(mContext);

        mCommonMethods.setApplicationToolbarMenu1(this, "TCC(Training Complete Certificate) Dashboard");

        EditText edt_serach_tcc_urn = findViewById(R.id.edt_serach_tcc_urn);
        ListView lst_tcc_details = findViewById(R.id.lst_tcc_details);

        edt_serach_tcc_urn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapterTCCDAshboard.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //get Data from DB
        lst_tcc_exam_details = db.getTCCAllDetails("");
        mAdapterTCCDAshboard = new AdapterTCCDAshboard(mContext, lst_tcc_exam_details);
        lst_tcc_details.setAdapter(mAdapterTCCDAshboard);
        mAdapterTCCDAshboard.notifyDataSetChanged();

        lst_tcc_details.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                TCC_ExamDetails_Activity.TCC_ExamDetails obj = lst_tcc_exam_details.get(position);

                if (obj.getStr_sync_status().equals("1")){
                    Intent intent = new Intent(mContext, CIF_TCC_UploadActivity.class);
                    intent.putExtra("URN", obj.getStrURN());
                    intent.putExtra("DASHBOARD", true);
                    startActivity(intent);
                }else{
                    mCommonMethods.showToast(mContext, "Synch Done!");
                }

                return false;
            }
        });
    }

    class AdapterTCCDAshboard extends BaseAdapter implements Filterable{

        private Context adapterContext;
        private ArrayList<TCC_ExamDetails_Activity.TCC_ExamDetails> mList, lstSearch;

        AdapterTCCDAshboard(Context adapterContext, ArrayList<TCC_ExamDetails_Activity.TCC_ExamDetails> mList) {
            this.adapterContext = adapterContext;
            this.mList = mList;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    final FilterResults oReturn = new FilterResults();
                    final ArrayList<TCC_ExamDetails_Activity.TCC_ExamDetails> results = new ArrayList<>();

                    if (lstSearch == null)
                        lstSearch = mList;

                    if (charSequence != null) {
                        if (lstSearch != null && lstSearch.size() > 0) {
                            for (final TCC_ExamDetails_Activity.TCC_ExamDetails g : lstSearch) {
                                if (g.getStrURN().toLowerCase().contains(charSequence.toString().toLowerCase()))
                                    results.add(g);
                            }
                        }
                        oReturn.values = results;
                    }
                    return oReturn;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    mList = (ArrayList<TCC_ExamDetails_Activity.TCC_ExamDetails>) results.values;
                    notifyDataSetChanged();
                }
            };
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        private class Holder{
            private TextView txt_tcc_URN, txt_tcc_existing_center, txt_tcc_Centre, txt_tcc_ExamDate;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Holder mHolder;
            // only inflate the view if it's null
            if (convertView == null) {
                convertView= LayoutInflater.from(adapterContext).inflate(R.layout.row_tcc_exam_dashboard, parent, false);
                mHolder = new Holder();

                // get text view
                mHolder.txt_tcc_URN = convertView.findViewById(R.id.txt_tcc_URN);
                mHolder.txt_tcc_existing_center = convertView.findViewById(R.id.txt_tcc_existing_center);
                mHolder.txt_tcc_Centre = convertView.findViewById(R.id.txt_tcc_Centre);
                mHolder.txt_tcc_ExamDate = convertView.findViewById(R.id.txt_tcc_ExamDate);
                convertView.setTag(mHolder);
            }else{
                mHolder = (Holder) convertView.getTag();
            }

            mHolder.txt_tcc_URN.setText(mList.get(position).getStrURN() == null ? "" : mList.get(position).getStrURN());
            mHolder.txt_tcc_existing_center.setText(mList.get(position).getStr_existing_center() == null ? "" : mList.get(position).getStr_existing_center());
            mHolder.txt_tcc_Centre.setText(mList.get(position).getStr_centre() == null ? "" : mList.get(position).getStr_centre());
            mHolder.txt_tcc_ExamDate.setText(mList.get(position).getStr_preferred_date() == null ? "" : mList.get(position).getStr_preferred_date());

            return convertView;
        }
    }

}


