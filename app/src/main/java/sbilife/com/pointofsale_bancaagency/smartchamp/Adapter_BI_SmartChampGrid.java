package sbilife.com.pointofsale_bancaagency.smartchamp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sbilife.com.pointofsale_bancaagency.R;


public class Adapter_BI_SmartChampGrid extends BaseAdapter {

    public static class ViewHolder {


        TextView tv_end_of_year;
        TextView tv_total_base_premium;
        TextView Guaranteed_additions;
        TextView tv_guaranteed_survival_benefit;
        TextView tv_guaranteed_surrender_value;
        TextView tv_guaranteed_death_benefit;
        TextView tv_guaranteed_maturity_benefit;
        TextView tv_not_guaranteed_maturity_benefit_4per;
        TextView tv_not_guaranteed_cash_bonus_4per;
        TextView tv_non_guaranteed_survival_benefit_4per;
        TextView tv_not_guaranteed_surrender_value_4per;
        TextView tv_not_guaranteed_maturity_benefit_8per;


        TextView tv_not_guaranteed_cash_bonus_8per;
        TextView tv_non_guaranteed_survival_benefit_8per;

        TextView tv_not_guaranteed_surrender_value_8per;

        TextView tv_total_survival_benefit_4per;
        TextView tv_total_survival_benefit_8per;
        TextView tv_not_guaranteed_survival_benefit_4per;
        TextView tv_not_guaranteed_survival_benefit_8per;

        TextView tv_not_guaranteed_death_benefit_4per;
        TextView tv_not_guaranteed_death_benefit_8per;


    }

    List<M_BI_SmartChamp_Adapter> allElementDetails;
    // LayoutInflater mInflater;
    Context mContext;

    private int[] colors = new int[]{Color.parseColor("#DCDBDB"),
            Color.parseColor("#E8E8E8")};
    //
    // private int[] colors = new int[] { Color.parseColor("#DCDBDB"),
    // Color.parseColor("#E8E8E8") };

    public Adapter_BI_SmartChampGrid(Context context, List<M_BI_SmartChamp_Adapter> results) {
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
            convertView = mInflater.inflate(R.layout.adapter_bi_grid_smart_champ,
                    null);
            holder = new ViewHolder();


            holder.tv_end_of_year = (TextView) convertView.findViewById(R.id.tv_adapter_bi_grid_smart_money_back_gold_end_year);
            holder.tv_total_base_premium = (TextView) convertView.findViewById(R.id.tv_adapter_bi_grid_smart_money_back_gold_total_base_premium);
            holder.Guaranteed_additions = (TextView) convertView.findViewById(R.id.tv_adapter_bi_grid_smart_money_back_gold_Guaranteed_additions);
            holder.tv_guaranteed_survival_benefit = (TextView) convertView.findViewById(R.id.tv_adapter_bi_grid_smart_money_back_gold_survival_benefit_guaranteed);
            holder.tv_guaranteed_surrender_value = (TextView) convertView.findViewById(R.id.tv_adapter_bi_grid_smart_money_back_gold_surrender_value_guaranteed);
            holder.tv_guaranteed_death_benefit = (TextView) convertView.findViewById(R.id.tv_adapter_bi_grid_smart_money_back_gold_death_benefit_guaranteed);
            holder.tv_guaranteed_maturity_benefit = (TextView) convertView.findViewById(R.id.tv_adapter_bi_grid_smart_money_back_gold_maturity_benefit_guaranteed);

            holder.tv_not_guaranteed_maturity_benefit_4per = (TextView) convertView.findViewById(R.id.tv_adapter_bi_grid_smart_money_back_gold_maturity_benefit_not_guaranteed_4_percentage);
            holder.tv_not_guaranteed_cash_bonus_4per = (TextView) convertView.findViewById(R.id.tv_adapter_bi_grid_smart_money_back_gold_cash_bonus_not_guaranteed_4_percentage);
            holder.tv_non_guaranteed_survival_benefit_4per = (TextView) convertView.findViewById(R.id.tv_non_guaranteed_survival_benefit_4per);
            holder.tv_not_guaranteed_surrender_value_4per = (TextView) convertView.findViewById(R.id.tv_adapter_bi_grid_smart_money_back_gold_surrender_value_not_guaranteed_4_percentage);

            holder.tv_not_guaranteed_maturity_benefit_8per = (TextView) convertView.findViewById(R.id.tv_adapter_bi_grid_smart_money_back_gold_maturity_benefit_not_guaranteed_8_percentage);


            holder.tv_not_guaranteed_cash_bonus_8per = (TextView) convertView.findViewById(R.id.tv_adapter_bi_grid_smart_money_back_gold_cash_bonus_not_guaranteed_8_percentage);

            holder.tv_non_guaranteed_survival_benefit_8per = (TextView) convertView.findViewById(R.id.tv_non_guaranteed_survival_benefit_8per);
            holder.tv_not_guaranteed_surrender_value_8per = (TextView) convertView.findViewById(R.id.tv_adapter_bi_grid_smart_money_back_gold_surrender_value_not_guaranteed_8_percentage);


            holder.tv_total_survival_benefit_4per = (TextView) convertView.findViewById(R.id.tv_total_survival_benefit_4per);
            holder.tv_total_survival_benefit_8per = (TextView) convertView.findViewById(R.id.tv_total_survival_benefit_8per);

            holder.tv_not_guaranteed_survival_benefit_4per = (TextView) convertView.findViewById(R.id.tv_adapter_bi_grid_smart_money_back_gold_survival_benefit_not_guaranteed_4_percentage);
            holder.tv_not_guaranteed_survival_benefit_8per = (TextView) convertView.findViewById(R.id.tv_adapter_bi_grid_smart_money_back_gold_survival_benefit_not_guaranteed_8_percentage);

            holder.tv_not_guaranteed_death_benefit_4per = (TextView) convertView.findViewById(R.id.tv_adapter_bi_grid_smart_money_back_gold_death_benefit_not_guaranteed_4_percentage);
            holder.tv_not_guaranteed_death_benefit_8per = (TextView) convertView.findViewById(R.id.tv_adapter_bi_grid_smart_money_back_gold_death_benefit_not_guaranteed_8_percentage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.tv_end_of_year.setText(String.valueOf(allElementDetails.get(position).getEnd_of_year()));
        holder.tv_total_base_premium.setText(String.valueOf(allElementDetails.get(position).getTotal_base_premium()));
        holder.Guaranteed_additions.setText(String.valueOf(allElementDetails.get(position).getGuaranteed_additions()));
        holder.tv_guaranteed_survival_benefit.setText(String.valueOf(allElementDetails.get(position).getGuaranteed_survival_benefit()));
        holder.tv_guaranteed_surrender_value.setText(String.valueOf(allElementDetails.get(position).getGuaranteed_surrender_value()));
        holder.tv_guaranteed_death_benefit.setText(String.valueOf(allElementDetails.get(position).getGuaranteed_death_benefit()));
        holder.tv_guaranteed_maturity_benefit.setText(String.valueOf(allElementDetails.get(position).getGuaranteed_maturity_benefit()));
        holder.tv_not_guaranteed_maturity_benefit_4per.setText(String.valueOf(allElementDetails.get(position).getNot_guaranteed_maturity_benefit_4per()));
        holder.tv_not_guaranteed_cash_bonus_4per.setText(String.valueOf(allElementDetails.get(position).getCash_bonus_4per()));
        holder.tv_non_guaranteed_survival_benefit_4per.setText(String.valueOf(allElementDetails.get(position).getNon_guaranteed_survival_benefit_4per()));
        holder.tv_not_guaranteed_surrender_value_4per.setText(String.valueOf(allElementDetails.get(position).getNot_guaranteed_surrender_value_4per()));
        holder.tv_not_guaranteed_maturity_benefit_8per.setText(String.valueOf(allElementDetails.get(position).getNot_guaranteed_maturity_benefit_8per()));


        holder.tv_not_guaranteed_cash_bonus_8per.setText(String.valueOf(allElementDetails.get(position).getCash_bonus_8per()));
        holder.tv_non_guaranteed_survival_benefit_8per.setText(String.valueOf(allElementDetails.get(position).getNon_guaranteed_survival_benefit_8per()));
        holder.tv_not_guaranteed_surrender_value_8per.setText(String.valueOf(allElementDetails.get(position).getNot_guaranteed_surrender_value_8per()));

        holder.tv_total_survival_benefit_4per.setText(String.valueOf(allElementDetails.get(position).getTotal_survival_benefit_4per()));
        holder.tv_total_survival_benefit_8per.setText(String.valueOf(allElementDetails.get(position).getTotal_survival_benefit_8per()));


        holder.tv_not_guaranteed_survival_benefit_4per.setText(String.valueOf(allElementDetails.get(position).getNot_guaranteed_survival_benefit_4per()));
        holder.tv_not_guaranteed_survival_benefit_8per.setText(String.valueOf(allElementDetails.get(position).getNot_guaranteed_survival_benefit_8per()));
        holder.tv_not_guaranteed_death_benefit_4per.setText(String.valueOf(allElementDetails.get(position).getNot_guaranteed_death_benefit_4per()));
        holder.tv_not_guaranteed_death_benefit_8per.setText(String.valueOf(allElementDetails.get(position).getNot_guaranteed_death_benefit_8per()));


        int colorPos = position % colors.length;
        convertView.setBackgroundColor(colors[colorPos]);

        return convertView;
    }


}
