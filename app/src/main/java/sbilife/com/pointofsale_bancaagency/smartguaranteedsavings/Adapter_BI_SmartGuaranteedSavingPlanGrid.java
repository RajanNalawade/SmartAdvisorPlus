package sbilife.com.pointofsale_bancaagency.smartguaranteedsavings;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sbilife.com.pointofsale_bancaagency.R;


class Adapter_BI_SmartGuaranteedSavingPlanGrid extends BaseAdapter {

    static class ViewHolder {
        TextView tv_policy_year;
        TextView tv_yearly_basic_premium;
        TextView tv_cumulative_premium;
        TextView tv_guaranteed_addition;
        TextView tv_guranteed_death_benefit;
        TextView tv_guranteed_maturity_benefit;
        TextView tv_guranteed_surrender_value;

    }

    private List<M_BI_SmartGuaranteed_Saving_Plan_Adapter> allElementDetails;
    // LayoutInflater mInflater;
    private Context mContext;

    private int[] colors = new int[]{Color.parseColor("#DCDBDB"),
            Color.parseColor("#E8E8E8")};
    //
    // private int[] colors = new int[] { Color.parseColor("#DCDBDB"),
    // Color.parseColor("#E8E8E8") };

    public Adapter_BI_SmartGuaranteedSavingPlanGrid(Context context, List<M_BI_SmartGuaranteed_Saving_Plan_Adapter> results) {
        allElementDetails = results;
        mContext = context;
        //mInflater = LayoutInflater.from(context);
    }

//	public Adapter_BI_grid(Context context, String[] results) {
//		allElementDetails = Arrays.asList(results);
//		mInflater = LayoutInflater.from(context);
//	}

    public int getCount() {
        // TODO Auto-generated method stub
        return allElementDetails.size();
    }


    public Object getItem(int position) {
        // TODO Auto-generated method stub
        //return allElementDetails.get(position);
        return null;
        //return null;
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
            convertView = mInflater.inflate(R.layout.adapter_bi_grid_smart_guaranteed_saving_plan,
                    null);
            holder = new ViewHolder();


            holder.tv_policy_year = convertView.findViewById(R.id.tv_adapter_bi_grid_smart_guaranteed_end_year);
            holder.tv_yearly_basic_premium = convertView.findViewById(R.id.tv_adapter_bi_grid_smart_guaranteed_yearly_basic_premium);
            holder.tv_cumulative_premium = convertView.findViewById(R.id.tv_adapter_bi_grid_smart_guaranteed_cumulative_premium);
            holder.tv_guaranteed_addition = convertView.findViewById(R.id.tv_adapter_bi_grid_smart_guaranteed_guaranteed_addition);
            holder.tv_guranteed_death_benefit = convertView.findViewById(R.id.tv_adapter_bi_grid_smart_guaranteed_guaranteed_death_benefit);
            holder.tv_guranteed_maturity_benefit = convertView.findViewById(R.id.tv_adapter_bi_grid_smart_guaranteed_guaranteed_maturity_benefit);
            holder.tv_guranteed_surrender_value = convertView.findViewById(R.id.tv_adapter_bi_grid_smart_guaranteed_guaranteed_surrender_value);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.tv_policy_year.setText(String.valueOf(allElementDetails.get(position).getEnd_of_year()));
        holder.tv_yearly_basic_premium.setText(String.valueOf(allElementDetails.get(position).getYearly_basic_premium()));
        holder.tv_cumulative_premium.setText(String.valueOf(allElementDetails.get(position).getCumulative_premium()));
        holder.tv_guaranteed_addition.setText(String.valueOf(allElementDetails.get(position).getGuaranteed_addition()));
        holder.tv_guranteed_death_benefit.setText(String.valueOf(allElementDetails.get(position).getGuaranteed_death_benefit()));
        holder.tv_guranteed_maturity_benefit.setText(String.valueOf(allElementDetails.get(position).getGuaranteed_maturity_benefit()));
        holder.tv_guranteed_surrender_value.setText(String.valueOf(allElementDetails.get(position).getGuaranteed_surrender_value()));


        int colorPos = position % colors.length;
        convertView.setBackgroundColor(colors[colorPos]);

        return convertView;
    }


}
