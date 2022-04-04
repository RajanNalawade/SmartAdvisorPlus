package sbilife.com.pointofsale_bancaagency;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;



class PagerAdapter extends FragmentPagerAdapter {
	 private List<Fragment> fragments;
	    /**
	     * @param fm
	     * @param fragments
	     */
	    public PagerAdapter(FragmentManager fm, List<Fragment> fragments) {
	        super(fm);
	        this.fragments = fragments;
	    }
	    /* (non-Javadoc)
	     * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
	     */
	    @Override
	    public Fragment getItem(int position) {
	        return this.fragments.get(position);
	    }
	 
	    /* (non-Javadoc)
	     * @see android.support.v4.view.PagerAdapter#getCount()
	     */
	    @Override
	    public int getCount() {
	        return this.fragments.size();
	    }

}