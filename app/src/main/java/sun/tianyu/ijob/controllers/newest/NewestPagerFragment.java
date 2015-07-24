package sun.tianyu.ijob.controllers.newest;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.couchbase.lite.Database;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.LiveQuery;
import com.couchbase.lite.Manager;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.android.AndroidContext;

import java.util.ArrayList;
import java.util.Map;

import sun.tianyu.ijob.IjobApplication;
import sun.tianyu.ijob.R;
import sun.tianyu.ijob.common.CommonFragment;
import sun.tianyu.ijob.common.DefautValues;

/**
 * Created by Developer on 15/07/22.
 */
public class NewestPagerFragment extends CommonFragment implements SwipeRefreshLayout.OnRefreshListener{
    public static final String CategoryNumberKey = "CategoryNum";
    SwipeRefreshLayout swipeLayout;
    ListView listView;
    NewestPagerListAdapter mListAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.newest_pager_fragment, container, false);
        Bundle args = getArguments();
        ((TextView) rootView.findViewById(R.id.text1)).setText(
                Integer.toString(args.getInt(CategoryNumberKey)));

        listView = (ListView)rootView.findViewById(R.id.newest_list);
//        String[] members = { "スポット1", "スポット2", "スポット3", "スポット4",
//                "スポット5", "スポット6", "スポット7",  "スポット8",  "スポット9",  "スポット10",  "スポット11" };
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_expandable_list_item_1, members);
//        listView.setAdapter(adapter);

        LiveQuery query = getQuery(getDatabase(), "0").toLiveQuery();
        mListAdapter = new NewestPagerListAdapter(getActivity(), query);

        listView.setAdapter(mListAdapter);

        swipeLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return rootView;
    }

    private Query getQuery(Database database, String listDocId) {
        com.couchbase.lite.View view = database.getView("jobs");
        if (view.getMap() == null) {
            Log.e("STYLOG","Null Null Null Null NUll");
            Mapper map = new Mapper() {
                @Override
                public void map(Map<String, Object> document, Emitter emitter) {
                    if (!"0".equals(document.get("offer_type"))) {
                        java.util.List<Object> keys = new ArrayList<Object>();
                        keys.add(document.get("offer_info"));
                        keys.add(document.get("offer_term"));
                        keys.add(document.get("offer_name"));
                        keys.add(document.get("created_at"));
                        emitter.emit(keys, document);
                    }
                }
            };
            view.setMap(map, "1");
        }

        Query query = view.createQuery();
        query.setDescending(true);
        return query;
    }

    // DBインスタンス取得
    private Database getDatabase() {
        return((IjobApplication)getActivity().getApplicationContext()).database;
    }

    @Override public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                swipeLayout.setRefreshing(false);
            }
        }, 5000);
    }




}
