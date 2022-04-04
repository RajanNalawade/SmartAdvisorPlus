package sbilife.com.pointofsale_bancaagency.products.smartplatinaassure;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sbilife.com.pointofsale_bancaagency.R;

public class Adapter_BI_SmartPlatinaAssureGridNew extends BaseAdapter {


    public static class ViewHolder {


        TextView tv_policy_year;
        TextView tv_yearly_basic_premium;
        TextView tv_SurvivalBenefits;
        TextView tv_OtherBenefitsifAny;
        TextView tv_guranteed_maturity_benefit;
        TextView tv_guranteed_death_benefit;
        TextView tv_guranteed_surrender_value;
        TextView tv_nonGuaranSurrenderValue;

    }

    List<M_BI_Smart_Platina_Assure> allElementDetails;
    // LayoutInflater mInflater;
    Context mContext;

    private int[] colors = new int[]{Color.parseColor("#DCDBDB"),
            Color.parseColor("#E8E8E8")};
    //
    // private int[] colors = new int[] { Color.parseColor("#DCDBDB"),
    // Color.parseColor("#E8E8E8") };

    public Adapter_BI_SmartPlatinaAssureGridNew(Context context, List<M_BI_Smart_Platina_Assure> results) {
        allElementDetails = results;
        mContext = context;
        //mInflater = LayoutInflater.from(context);
    }

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
            convertView = mInflater.inflate(R.layout.adapter_bi_grid_smart_platina_assure,
                    null);
            holder = new ViewHolder();


            holder.tv_policy_year = (TextView) convertView.findViewById(R.id.tv_adapter_bi_grid_smart_swadhan_plus_end_year);
            ;
            holder.tv_yearly_basic_premium = (TextView) convertView.findViewById(R.id.tv_adapter_bi_grid_smart_swadhan_plus_yearly_basic_premium);
            holder.tv_SurvivalBenefits = (TextView) convertView.findViewById(R.id.tv_adapter_bi_grid_smart_swadhan_plus_SurvivalBenefits);
            holder.tv_OtherBenefitsifAny = (TextView) convertView.findViewById(R.id.tv_adapter_bi_grid_smart_swadhan_plus_OtherBenefitsifAny);
            holder.tv_guranteed_maturity_benefit = (TextView) convertView.findViewById(R.id.tv_adapter_bi_grid_smart_swadhan_plus_guaranteed_maturity_benefit);
            holder.tv_guranteed_death_benefit = (TextView) convertView.findViewById(R.id.tv_adapter_bi_grid_smart_swadhan_plus_guaranteed_death_benefit);
            holder.tv_guranteed_surrender_value = (TextView) convertView.findViewById(R.id.tv_adapter_bi_grid_smart_swadhan_plus_guaranteed_surrender_value);
            holder.tv_nonGuaranSurrenderValue = (TextView) convertView.findViewById(R.id.tv_adapter_bi_grid_smart_swadhan_plus_nonGuaranSurrenderValue);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.tv_policy_year.setText(String.valueOf(allElementDetails.get(position).getEnd_of_year()));
        holder.tv_yearly_basic_premium.setText(String.valueOf(allElementDetails.get(position).getYearly_basic_premium()));
        holder.tv_SurvivalBenefits.setText(String.valueOf(allElementDetails.get(position).getSurvivalBenefits()));
        holder.tv_OtherBenefitsifAny.setText(String.valueOf(allElementDetails.get(position).getOtherBenefitsifAny()));
        holder.tv_guranteed_maturity_benefit.setText(String.valueOf(allElementDetails.get(position).getGuaranteed_maturity_benefit()));
        holder.tv_guranteed_death_benefit.setText(String.valueOf(allElementDetails.get(position).getGuaranteed_death_benefit()));
        holder.tv_guranteed_surrender_value.setText(String.valueOf(allElementDetails.get(position).getGuaranteed_surrender_value()));
        holder.tv_nonGuaranSurrenderValue.setText(String.valueOf(allElementDetails.get(position).getNonGuaranSurrenderValue()));


        int colorPos = position % colors.length;
        convertView.setBackgroundColor(colors[colorPos]);

        return convertView;
    }


}
