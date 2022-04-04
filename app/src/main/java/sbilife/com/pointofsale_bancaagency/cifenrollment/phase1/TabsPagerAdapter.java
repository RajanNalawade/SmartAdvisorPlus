package sbilife.com.pointofsale_bancaagency.cifenrollment.phase1;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

// An equivalent ViewPager2 adapter class
class TabsPagerAdapter extends FragmentStateAdapter {

    private int id;

    public TabsPagerAdapter(FragmentActivity fa, int value) {
        super(fa);
        id = value;
    }

    @Override
    public Fragment createFragment(int index) {
        switch (index) {
            case 0:
                return new Fragment_PersonalDetails();
            case 1:
                return new Fragment_ContactDetails();
            case 2:
                return new Fragment_Qualification();
            case 3:
                return new Fragment_ExamDetails();
            case 4:
                return new Fragment_Identification();
            case 5:
                return new Fragment_Preview();

            default:
                return null;
        }

    }

    @Override
    public int getItemCount() {
        return id;
    }
}
