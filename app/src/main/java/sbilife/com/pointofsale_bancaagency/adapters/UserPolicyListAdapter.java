package sbilife.com.pointofsale_bancaagency.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;

public class UserPolicyListAdapter extends RecyclerView.Adapter<UserPolicyListAdapter.ViewHolderAdapter> {

    private ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio> renewalListGioSummaries;
    private OnItemClickListener listener;

    public UserPolicyListAdapter(ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio> renewalListGioSummaries, OnItemClickListener listener) {
        this.renewalListGioSummaries = renewalListGioSummaries;

        this.listener = listener;

    }


    @Override
    public int getItemCount() {
        return renewalListGioSummaries.size();
    }

    @Override
    public ViewHolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.policies_list_item_layout, parent, false);

        return new ViewHolderAdapter(mView);
    }

    @Override
    public void onBindViewHolder(final ViewHolderAdapter holder, int position) {

        holder.bind(renewalListGioSummaries.get(position), listener);
        /*ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio> renewalListGioSummary = renewalListGioSummaries.get(pincode);

        holder.txtSummaryCity.setText(pincode);
        holder.txtSummaryCount.setText(renewalListGioSummary.size()+"");
        holder.txtSummaryAmount.setText(getTotalAmount(renewalListGioSummary)+"");*/

    }

    public class ViewHolderAdapter extends RecyclerView.ViewHolder {


        private final TextView policy_person_name, policy_person_city, policy_person_probale_commission;

        ViewHolderAdapter(View v) {
            super(v);
            policy_person_name = v.findViewById(R.id.policy_person_name);
            policy_person_city = v.findViewById(R.id.policy_person_city);
            policy_person_probale_commission = v.findViewById(R.id.policy_person_probale_commission);

        }

        public void bind(final ParseXML.AgentPoliciesRenewalListMonthwiseGio agentPoliciesRenewalListMonthwiseGio, final OnItemClickListener listener) {

            policy_person_name.setText(agentPoliciesRenewalListMonthwiseGio.getPOLICYNUMBER());
            policy_person_city.setText(agentPoliciesRenewalListMonthwiseGio.getPERMANENTCITY());
            //txtSummaryAmount.setText(getTotalAmount(renewalListGioSummary)+"");
            policy_person_probale_commission.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(agentPoliciesRenewalListMonthwiseGio);

                }

            });
        }

    }


    public interface OnItemClickListener {
        void onItemClick(ParseXML.AgentPoliciesRenewalListMonthwiseGio agentPoliciesRenewalListMonthwiseGio);

    }
}
