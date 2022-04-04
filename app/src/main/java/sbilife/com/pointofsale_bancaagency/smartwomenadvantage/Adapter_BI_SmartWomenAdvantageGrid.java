package sbilife.com.pointofsale_bancaagency.smartwomenadvantage;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sbilife.com.pointofsale_bancaagency.R;

class Adapter_BI_SmartWomenAdvantageGrid extends BaseAdapter {

    static class ViewHolder {
        TextView tv_policy_year;
        TextView tv_death_gurantee;
        TextView tv_benefit_payable_at_death_4_percentage;
        TextView tv_benefit_payable_at_death_8_percentage;
        TextView tv_critical_illness_benefit_gurantee;
        TextView tv_critical_illness_benefit_non_gurantee_4_percentage;
        TextView tv_critical_illness_benefit_non_gurantee_8_percentage;
        TextView tv_maturity_benefit_gurantee;
        TextView tv_maturity_benefit_non_gurantee_4_percentage;
        TextView tv_maturity_benefit_non_gurantee_8_percentage;
        TextView tv_surrender_value_gurantee;
        TextView tv_surrender_value_ssv_4_percentage;
        TextView tv_surrender_value_ssv_8_percentage;
    }

    private List<M_BI_SmartWomenAdvantageGrid_Adpter> allElementDetails;
    // LayoutInflater mInflater;
    private Context mContext;

    private int[] colors = new int[]{Color.parseColor("#DCDBDB"),
            Color.parseColor("#E8E8E8")};
    //
    // private int[] colors = new int[] { Color.parseColor("#DCDBDB"),
    // Color.parseColor("#E8E8E8") };

    public Adapter_BI_SmartWomenAdvantageGrid(Context context, List<M_BI_SmartWomenAdvantageGrid_Adpter> results) {
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
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.adpter_bi_grid_smart_women_advantage,
                    null);
            holder = new ViewHolder();
            holder.tv_policy_year = convertView.findViewById(R.id.tv_policy_year);
            holder.tv_death_gurantee = convertView.findViewById(R.id.tv_death_gurantee);
            holder.tv_benefit_payable_at_death_4_percentage = convertView.findViewById(R.id.tv_benefit_payable_at_death_4_percentage);
            holder.tv_benefit_payable_at_death_8_percentage = convertView.findViewById(R.id.tv_benefit_payable_at_death_8_percentage);
            holder.tv_critical_illness_benefit_gurantee = convertView.findViewById(R.id.tv_critical_illness_benefit_gurantee);
            holder.tv_critical_illness_benefit_non_gurantee_4_percentage = convertView.findViewById(R.id.tv_critical_illness_benefit_non_gurantee_4_percentage);
            holder.tv_critical_illness_benefit_non_gurantee_8_percentage = convertView.findViewById(R.id.tv_critical_illness_benefit_non_gurantee_8_percentage);
            holder.tv_maturity_benefit_gurantee = convertView.findViewById(R.id.tv_maturity_benefit_gurantee);
            holder.tv_maturity_benefit_non_gurantee_4_percentage = convertView.findViewById(R.id.tv_maturity_benefit_non_gurantee_4_percentage);
            holder.tv_maturity_benefit_non_gurantee_8_percentage = convertView.findViewById(R.id.tv_maturity_benefit_non_gurantee_8_percentage);
            holder.tv_surrender_value_gurantee = convertView.findViewById(R.id.tv_surrender_value_gurantee);
            holder.tv_surrender_value_ssv_4_percentage = convertView.findViewById(R.id.tv_surrender_value_ssv_4_percentage);
            holder.tv_surrender_value_ssv_8_percentage = convertView.findViewById(R.id.tv_surrender_value_ssv_8_percentage);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.tv_policy_year.setText(String.valueOf(allElementDetails.get(position).getPolicy_year()));
        holder.tv_death_gurantee.setText(String.valueOf(allElementDetails.get(position).getDeath_gurantee()));
        holder.tv_benefit_payable_at_death_4_percentage.setText(String.valueOf(allElementDetails.get(position).getBenefit_payable_at_death_4_percentage()));
        holder.tv_benefit_payable_at_death_8_percentage.setText(String.valueOf(allElementDetails.get(position).getBenefit_payable_at_death_8_percentage()));
        holder.tv_critical_illness_benefit_gurantee.setText(String.valueOf(allElementDetails.get(position).getCritical_illness_gurantee()));
        holder.tv_critical_illness_benefit_non_gurantee_4_percentage.setText(String.valueOf(allElementDetails.get(position).getCritical_illness_non_gurantee_4_percentage()));
        holder.tv_critical_illness_benefit_non_gurantee_8_percentage.setText(String.valueOf(allElementDetails.get(position).getCritical_illness_non_gurantee_8_percentage()));
        holder.tv_maturity_benefit_gurantee.setText(String.valueOf(allElementDetails.get(position).getMaturity_benefit_gurantee()));
        holder.tv_maturity_benefit_non_gurantee_4_percentage.setText(String.valueOf(allElementDetails.get(position).getMaturity_benefit_non_gurantee_4_percentage()));
        holder.tv_maturity_benefit_non_gurantee_8_percentage.setText(String.valueOf(allElementDetails.get(position).getMaturity_benefit_non_gurantee_8_percentage()));
        holder.tv_surrender_value_gurantee.setText(String.valueOf(allElementDetails.get(position).getSurrender_value_gurantee()));
        holder.tv_surrender_value_ssv_4_percentage.setText(String.valueOf(allElementDetails.get(position).getSurrender_value_ssv_4_percentage()));
        holder.tv_surrender_value_ssv_8_percentage.setText(String.valueOf(allElementDetails.get(position).getSurrender_value_ssv_8_percentage()));

        int colorPos = position % colors.length;
        convertView.setBackgroundColor(colors[colorPos]);

        return convertView;
    }


}

