package sbilife.com.pointofsale_bancaagency.home.dashboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

class ViewPagerAdapter extends FragmentPagerAdapter {

    private int PAGE_COUNT;
    private String renewedpolicy_trad1;
    private String renewedpolicy_ulip1;
    private String newpolicy_trad1;
    private String newpolicy_ulip1;
    private String renewalpremium_trad1;
    private String renewalpremium_ulip1;
    private String newbusinesspremium_trad1;
    private String newbusinesspremium_ulip1;
    private String strtype1;

    public ViewPagerAdapter(FragmentManager fm, String renewedpolicy_trad, String renewedpolicy_ulip, String newpolicy_trad, String newpolicy_ulip, String renewalpremium_trad, String renewalpremium_ulip, String newbusinesspremium_trad, String newbusinesspremium_ulip, String strtype) {
        super(fm);
        renewedpolicy_trad1 = renewedpolicy_trad;
        renewedpolicy_ulip1 = renewedpolicy_ulip;
        newpolicy_trad1 = newpolicy_trad;
        newpolicy_ulip1 = newpolicy_ulip;
        renewalpremium_trad1 = renewalpremium_trad;
        renewalpremium_ulip1 = renewalpremium_ulip;
        newbusinesspremium_trad1 = newbusinesspremium_trad;
        newbusinesspremium_ulip1 = newbusinesspremium_ulip;
        strtype1 = strtype;

        if (strtype1.equalsIgnoreCase("CIF")) {
            PAGE_COUNT = 4;
        } else {
            PAGE_COUNT = 2;
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            // Open FragmentTab1.java
            case 0:
                RenewedPolicyPieFragment fragmenttab1 = new RenewedPolicyPieFragment();
                Bundle bundle4 = new Bundle();
                bundle4.putString("renewedpolicy_trad1", renewedpolicy_trad1);
                bundle4.putString("renewedpolicy_ulip1", renewedpolicy_ulip1);
                bundle4.putString("strtype1", strtype1);
                fragmenttab1.setArguments(bundle4);
                return fragmenttab1;

            // Open FragmentTab2.java
            case 1:
                NewPolicyPieFragment fragmenttab2 = new NewPolicyPieFragment();
                Bundle bundle = new Bundle();
                bundle.putString("newpolicy_trad1", newpolicy_trad1);
                bundle.putString("newpolicy_ulip1", newpolicy_ulip1);
                bundle.putString("strtype1", strtype1);
                fragmenttab2.setArguments(bundle);
                return fragmenttab2;

            // Open FragmentTab3.java
            case 2:
                RenewalPremiumPieFragment fragmenttab3 = new RenewalPremiumPieFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putString("renewalpremium_trad1", renewalpremium_trad1);
                bundle2.putString("renewalpremium_ulip1", renewalpremium_ulip1);
                bundle2.putString("strtype1", strtype1);
                fragmenttab3.setArguments(bundle2);
                return fragmenttab3;

            case 3:
                NewBusinessPremiumPieFragment fragmenttab4 = new NewBusinessPremiumPieFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString("renewalpremium_trad1", renewalpremium_trad1);
                bundle1.putString("renewalpremium_ulip1", renewalpremium_ulip1);
                bundle1.putString("newbusinesspremium_trad1", newbusinesspremium_trad1);
                bundle1.putString("newbusinesspremium_ulip1", newbusinesspremium_ulip1);
                bundle1.putString("strtype1", strtype1);
                fragmenttab4.setArguments(bundle1);
                return fragmenttab4;
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        // Tab Titles
        String[] tabtitles;
        if (strtype1.equals("CIF")) {
            tabtitles = new String[]{"Renewal Policy Count", "New Policy Count", "Renewal Premium Amount", "New Business Premium Amount"};
        } else {
            tabtitles = new String[]{"MTD Report", "YTD Report"};
        }

        return tabtitles[position];
    }
}
