package sbilife.com.pointofsale_bancaagency.saralpension;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sbilife.com.pointofsale_bancaagency.R;

class Adapter_BI_SaralPensionGrid extends BaseAdapter {

    static class ViewHolder {
        TextView tv_year_of_death;
        TextView tv_guaranteed_death_benefit;
        TextView tv_bonuses_non_guarantee_4pa;
        TextView tv_bonuses_non_guarantee_8pa;
        TextView tv_vesting_benefit_4pa;
        TextView tv_vesting_benefit_8pa;

    }


    private List<M_BI_SaralPensionGrid_Adpter> allElementDetails;
    // LayoutInflater mInflater;
    private Context mContext;

    private int[] colors = new int[]{Color.parseColor("#DCDBDB"),
            Color.parseColor("#E8E8E8")};

    public Adapter_BI_SaralPensionGrid(Context context, List<M_BI_SaralPensionGrid_Adpter> results) {
        allElementDetails = results;
        mContext = context;
    }


    public int getCount() {
        // TODO Auto-generated method stub
        return allElementDetails.size();
    }


    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }


    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.adapter_bi_grid_saral_pension,
                    null);
            holder = new ViewHolder();
            holder.tv_year_of_death = convertView.findViewById(R.id.tv_year_of_death);
            holder.tv_guaranteed_death_benefit = convertView.findViewById(R.id.tv_guaranteed_death_benefit);
            holder.tv_bonuses_non_guarantee_4pa = convertView.findViewById(R.id.tv_bonuses_non_guarantee_4pa);
            holder.tv_bonuses_non_guarantee_8pa = convertView.findViewById(R.id.tv_bonuses_non_guarantee_8pa);
            holder.tv_vesting_benefit_4pa = convertView.findViewById(R.id.tv_vesting_benefit_4pa);
            holder.tv_vesting_benefit_8pa = convertView.findViewById(R.id.tv_vesting_benefit_8pa);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_year_of_death.setText(String.valueOf(allElementDetails.get(position).getYear_of_death()));
        holder.tv_guaranteed_death_benefit.setText(String.valueOf(allElementDetails.get(position).getGuaranteed_death_benefit()));
        holder.tv_bonuses_non_guarantee_4pa.setText(String.valueOf(allElementDetails.get(position).getBonuses_non_guarantee_4pa()));
        holder.tv_bonuses_non_guarantee_8pa.setText(String.valueOf(allElementDetails.get(position).getBonuses_non_guarantee_8pa()));
        holder.tv_vesting_benefit_4pa.setText(String.valueOf(allElementDetails.get(position).getVesting_benefit_4pa()));
        holder.tv_vesting_benefit_8pa.setText(String.valueOf(allElementDetails.get(position).getVesting_benefit_8pa()));

        int colorPos = position % colors.length;
        convertView.setBackgroundColor(colors[colorPos]);

        return convertView;

    }

}
