package sbilife.com.pointofsale_bancaagency.products.smartplatinaplus;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sbilife.com.pointofsale_bancaagency.R;


public class Adapter_BI_SmartPlatinaPlusGrid extends BaseAdapter {

    public static class ViewHolder {


        TextView tv_end_of_year;
        TextView tv_yearly_basic_premium;
        TextView tv_SurvivalBenefit;
        TextView tv_OtherBenefit;
        TextView tv_guaranteed_death_benefit;
        TextView tv_guaranteed_maturity_benefit;
        TextView tv_guaranteed_surrender_value;
        TextView tv_SpecialSurrenderValue;

    }

    List<M_BI_SmartPlatinaPlus_Adapter> allElementDetails;
    // LayoutInflater mInflater;
    Context mContext;

    private int[] colors = new int[]{Color.parseColor("#DCDBDB"),
            Color.parseColor("#E8E8E8")};
    //
    // private int[] colors = new int[] { Color.parseColor("#DCDBDB"),
    // Color.parseColor("#E8E8E8") };

    public Adapter_BI_SmartPlatinaPlusGrid(Context context, List<M_BI_SmartPlatinaPlus_Adapter> results) {
        allElementDetails = results;
        mContext = context;
        //mInflater = LayoutInflater.from(context);
    }

    //	public Adapter_BI_grid(Context context, String[] results) {
//		allElementDetails = Arrays.asList(results);
//		mInflater = LayoutInflater.from(context);
//	}
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return allElementDetails.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        //return allElementDetails.get(position);
        return null;
        //return null;
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
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.adapter_bi_grid_smart_platinaplus,
                    null);
            holder = new ViewHolder();


            holder.tv_end_of_year = (TextView) convertView.findViewById(R.id.tv_adapter_bi_grid_smart_guaranteed_end_year);
            ;
            holder.tv_yearly_basic_premium = (TextView) convertView.findViewById(R.id.tv_adapter_bi_grid_smart_guaranteed_yearly_basic_premium);
            holder.tv_SurvivalBenefit = (TextView) convertView.findViewById(R.id.tv_adapter_bi_grid_smart_guaranteed_cumulative_premium);
            holder.tv_OtherBenefit = (TextView) convertView.findViewById(R.id.tv_adapter_bi_grid_smart_guaranteed_guaranteed_addition);
            holder.tv_guaranteed_death_benefit = (TextView) convertView.findViewById(R.id.tv_adapter_bi_grid_smart_guaranteed_guaranteed_death_benefit);
            holder.tv_guaranteed_maturity_benefit = (TextView) convertView.findViewById(R.id.tv_adapter_bi_grid_smart_guaranteed_guaranteed_maturity_benefit);
            holder.tv_guaranteed_surrender_value = (TextView) convertView.findViewById(R.id.tv_adapter_bi_grid_smart_guaranteed_guaranteed_surrender_value);
            holder.tv_SpecialSurrenderValue = (TextView) convertView.findViewById(R.id.tv_adapter_bi_grid_special_surrender_value);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.tv_end_of_year.setText(String.valueOf(allElementDetails.get(position).getEnd_of_year()));
        holder.tv_yearly_basic_premium.setText(String.valueOf(allElementDetails.get(position).getYearly_basic_premium()));
        holder.tv_SurvivalBenefit.setText(String.valueOf(allElementDetails.get(position).getSurvivalBenefit()));
        holder.tv_OtherBenefit.setText(String.valueOf(allElementDetails.get(position).getOtherBenefit()));
        holder.tv_guaranteed_death_benefit.setText(String.valueOf(allElementDetails.get(position).getGuaranteed_death_benefit()));
        holder.tv_guaranteed_maturity_benefit.setText(String.valueOf(allElementDetails.get(position).getGuaranteed_maturity_benefit()));
        holder.tv_guaranteed_surrender_value.setText(String.valueOf(allElementDetails.get(position).getGuaranteed_surrender_value()));
        holder.tv_SpecialSurrenderValue.setText(String.valueOf(allElementDetails.get(position).getSpecialSurrenderValue()));


        int colorPos = position % colors.length;
        convertView.setBackgroundColor(colors[colorPos]);

        return convertView;
    }


}
