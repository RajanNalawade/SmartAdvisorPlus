package sbilife.com.pointofsale_bancaagency.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.reports.commonreports.PolicyHolderLocatorMapFragmentActivity;

public class PolicySummaryAdapter extends RecyclerView.Adapter<PolicySummaryAdapter.ViewHolderAdapter> {

//    private ArrayList<ParseXML.RenewalListGioSummary> renewalListGioSummaries;
//
//    public PolicySummaryAdapter(ArrayList<ParseXML.RenewalListGioSummary> renewalListGioSummaries) {
//        this.renewalListGioSummaries = renewalListGioSummaries;
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return renewalListGioSummaries.size();
//    }
//
//    @Override
//    public ViewHolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
//        View mView = LayoutInflater.from(parent.getContext()).inflate(
//                R.layout.summary_item_layout, parent, false);
//
//        return new ViewHolderAdapter(mView);
//    }
//
//    @Override
//    public void onBindViewHolder(final ViewHolderAdapter holder, int position) {
//        ParseXML.RenewalListGioSummary  renewalListGioSummary = renewalListGioSummaries.get(position);
//
//        holder.txtSummaryCity.setText(renewalListGioSummary.getCITY());
//        holder.txtSummaryCount.setText(renewalListGioSummary.getTOTAL_COUNT());
//        holder.txtSummaryAmount.setText(renewalListGioSummary.getTOTAL_AMOUNT());
//
//    }
//
//    public class ViewHolderAdapter extends RecyclerView.ViewHolder {
//
//
//        private final TextView txtSummaryCity, txtSummaryCount, txtSummaryAmount;
//
//        ViewHolderAdapter(View v) {
//            super(v);
//            txtSummaryCity = v.findViewById(R.id.summary_city_name);
//            txtSummaryCount = v.findViewById(R.id.summary_count);
//            txtSummaryAmount = v.findViewById(R.id.summary_amount);
//
//        }
//
//    }
    private HashMap<String, ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio>> renewalListGioSummaries;
    private ArrayList<String> cities;
    private OnItemClickListener listener;

    public PolicySummaryAdapter(HashMap<String, ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio>> renewalListGioSummaries,OnItemClickListener listener) {
        this.renewalListGioSummaries = renewalListGioSummaries;

        cities = new ArrayList<>(renewalListGioSummaries.keySet());
        this.listener = listener;

    }


    @Override
    public int getItemCount() {
        return renewalListGioSummaries.size();
    }

    @Override
    public ViewHolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.summary_item_layout, parent, false);

        return new ViewHolderAdapter(mView);
    }

    @Override
    public void onBindViewHolder(final ViewHolderAdapter holder, int position) {
        String city = cities.get(position);

        holder.bind(city, listener);
        /*ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio> renewalListGioSummary = renewalListGioSummaries.get(pincode);

        holder.txtSummaryCity.setText(pincode);
        holder.txtSummaryCount.setText(renewalListGioSummary.size()+"");
        holder.txtSummaryAmount.setText(getTotalAmount(renewalListGioSummary)+"");*/

    }

    public class ViewHolderAdapter extends RecyclerView.ViewHolder {


        private final TextView txtSummaryCity, txtSummaryCount, txtSummaryAmount;

        ViewHolderAdapter(View v) {
            super(v);
            txtSummaryCity = v.findViewById(R.id.summary_city_name);
            txtSummaryCount = v.findViewById(R.id.summary_count);
            txtSummaryAmount = v.findViewById(R.id.summary_amount);

        }

        public void bind(final String city, final OnItemClickListener listener) {

            final ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio> renewalListGioSummary = renewalListGioSummaries.get(city);

            txtSummaryCity.setText(city.toUpperCase());
            txtSummaryCount.setText(renewalListGioSummary.size()+"");
            txtSummaryAmount.setText(getTotalAmount(renewalListGioSummary));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(renewalListGioSummary);

                }

            });
        }

    }

    private String getTotalAmount(ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio> renewalListGioSummary){
        double totalPoliciesAmount = 0;
        for (ParseXML.AgentPoliciesRenewalListMonthwiseGio agentPoliciesRenewalListMonthwiseGio : renewalListGioSummary){
            totalPoliciesAmount += Double.parseDouble(agentPoliciesRenewalListMonthwiseGio.PREMIUMGROSSAMOUNT);
        }
        DecimalFormat df2 = new DecimalFormat("#.##");

        return df2.format(totalPoliciesAmount);
    }

    public interface OnItemClickListener {
        void onItemClick(ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio> renewalListGioSummary);

    }
}
