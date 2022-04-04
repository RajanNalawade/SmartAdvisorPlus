package sbilife.com.pointofsale_bancaagency.utility;

import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class GridHeight {

    public void getheight(GridView adapter, String policy_term){

        ListAdapter gridViewAdapter = adapter.getAdapter();

        int totalHeight = 0;
        int items = gridViewAdapter.getCount();
        int rows = Integer.parseInt(policy_term);

        View listItem = gridViewAdapter.getView(0, null, adapter);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        if (items > adapter.getNumColumns()) {
            totalHeight = totalHeight * rows;
        }

        ViewGroup.LayoutParams params = adapter.getLayoutParams();
        params.height = totalHeight;
        adapter.setLayoutParams(params);

        }
    public void getheight_Listview(ListView adapter, String policy_term){

        ListAdapter gridViewAdapter = adapter.getAdapter();

        int totalHeight = 0;
        int items = gridViewAdapter.getCount();
        int rows = Integer.parseInt(policy_term);

        View listItem = gridViewAdapter.getView(0, null, adapter);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        if (items > adapter.getCount()) {
            totalHeight = totalHeight * rows;
        }

        ViewGroup.LayoutParams params = adapter.getLayoutParams();
        params.height = totalHeight;
        adapter.setLayoutParams(params);

    }
}
