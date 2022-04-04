package sbilife.com.pointofsale_bancaagency.home.lmcorner;

import android.annotation.TargetApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

public class ContestsLMActivity extends AppCompatActivity {
    private CommonMethods commonMethods;
    private Context context;
    private RecyclerView recyclerview;
    private SelectedAdapter selectedAdapter;
    private ArrayList<LMContestsValuesModel> globalDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_contests_l_m);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        commonMethods = new CommonMethods();
        commonMethods.setApplicationToolbarMenu(this, "Contests");
        context = this;
        globalDataList = new ArrayList<>();
        String userType = commonMethods.GetUserType(context);

        /*if (userType.equalsIgnoreCase("AGENT")) {
            globalDataList.add(new LMContestsValuesModel(R.drawable.shubh_aarambh_banner, "Shubh Aarambh",
                    null, false, true, "https://drive.google.com/file/d/1jfmaTxr9OdPuJifEPrfI4EamtGfIjuCg/view?usp=sharing"));
        }*/
        globalDataList.add(new LMContestsValuesModel(R.drawable.banner_jotc, "Contest",
                null, false, true, "https://drive.google.com/file/d/1gDtgw_ED6Pjr9edP_OuR9Jfp326gQ4eW/view?usp=sharing"));

//        globalDataList.add(new LMContestsValuesModel(R.drawable.lm_club_poster, "Contest",
        globalDataList.add(new LMContestsValuesModel(R.drawable.lm_club_banner, "Contest",
                null, false, true, "https://drive.google.com/file/d/1XGOiA6OjRvV0iZlYXyg4wREddgS8jJBt/view?usp=sharing"));

       /* globalDataList.add(new LMContestsValuesModel(R.drawable.banner_jotc_booster, "JOTC Contest",
                null, false, false, ""));*/

        globalDataList.add(new LMContestsValuesModel(R.drawable.cruise_glory_ebandhan_banner,
                "Cruise Glory Banner",
                null, false, false, ""));
        recyclerview = findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        // set LayoutManager to RecyclerView
        recyclerview.setLayoutManager(linearLayoutManager);
        // call the constructor of CustomAdapter to send the reference and data to Adapter

        selectedAdapter = new SelectedAdapter(globalDataList);
        recyclerview.setAdapter(selectedAdapter);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
    }

    public class SelectedAdapter extends RecyclerView.Adapter<SelectedAdapter.
            ViewHolderAdapter> {

        private ArrayList<LMContestsValuesModel> lstAdapterList;

        SelectedAdapter(ArrayList<LMContestsValuesModel> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
        }

        @Override
        public int getItemCount() {
            System.out.println("SelectedAdapter.getItemCount:" + lstAdapterList.size());
            return lstAdapterList.size();
        }

        @Override
        public ViewHolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_item_contests_lm, parent, false);

            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(final ViewHolderAdapter holder, final int position) {
            //holder.IVbannerContest.setText(lstAdapterList.get(position).getNBM_PROPOSAL_NO());
            holder.IVbannerContest.setImageDrawable(context.getResources().getDrawable(lstAdapterList.get(position).getIconDrawable()));

            holder.IVbannerContest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    Class aClass = lstAdapterList.get(position).getaClass();
                    if (aClass != null) {
                    } else {
                        boolean isDriveLink = lstAdapterList.get(position).isDriveLink();
                        if (isDriveLink) {
                            commonMethods.loadDriveURL(lstAdapterList.get(position).getLink(), context);
                        }
                    }
                }
            });

        }


        public class ViewHolderAdapter extends RecyclerView.ViewHolder {
            private ImageView IVbannerContest;

            ViewHolderAdapter(View v) {
                super(v);
                IVbannerContest = v.findViewById(R.id.IVbannerContest);
            }

        }

    }

    class LMContestsValuesModel {
        private final int iconDrawable;
        private final String strMenuTitle;
        private final Class aClass;
        private final boolean homeTag;
        private final boolean driveLink;
        private final String link;

        LMContestsValuesModel(int iconDrawable, String strMenuTitle, Class aClass, boolean homeTag,
                              boolean driveLink, String link) {
            this.strMenuTitle = strMenuTitle;
            this.iconDrawable = iconDrawable;
            this.aClass = aClass;
            this.homeTag = homeTag;
            this.driveLink = driveLink;
            this.link = link;
        }

        Class getaClass() {
            return aClass;
        }

        boolean isHomeTag() {
            return homeTag;
        }

        boolean isDriveLink() {
            return driveLink;
        }

        String getLink() {
            return link;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LMContestsValuesModel lmContestsValuesModel = (LMContestsValuesModel) o;
            return iconDrawable == lmContestsValuesModel.iconDrawable &&
                    strMenuTitle.equals(lmContestsValuesModel.strMenuTitle);
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public int hashCode() {
            return Objects.hash(iconDrawable, strMenuTitle);
        }

        int getIconDrawable() {
            return iconDrawable;
        }


        String getStrMenuTitle() {
            return strMenuTitle;
        }

    }
}