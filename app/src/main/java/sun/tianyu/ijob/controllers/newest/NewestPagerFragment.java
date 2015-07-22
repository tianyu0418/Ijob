package sun.tianyu.ijob.controllers.newest;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import sun.tianyu.ijob.R;
import sun.tianyu.ijob.common.CommonFragment;

/**
 * Created by Developer on 15/07/22.
 */
public class NewestPagerFragment extends CommonFragment implements SwipeRefreshLayout.OnRefreshListener{
    public static final String CategoryNumberKey = "CategoryNum";
    SwipeRefreshLayout swipeLayout;
    ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
                R.layout.newest_pager_fragment, container, false);
        Bundle args = getArguments();
        ((TextView) rootView.findViewById(R.id.text1)).setText(
                Integer.toString(args.getInt(CategoryNumberKey)));

        listView = (ListView)rootView.findViewById(R.id.newest_list);
        String[] members = { "スポット1", "スポット2", "スポット3", "スポット4",
                "スポット5", "スポット6", "スポット7",  "スポット8",  "スポット9",  "スポット10",  "スポット11" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_expandable_list_item_1, members);
        listView.setAdapter(adapter);

        swipeLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return rootView;
    }

    @Override public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                swipeLayout.setRefreshing(false);
            }
        }, 5000);
    }
}
