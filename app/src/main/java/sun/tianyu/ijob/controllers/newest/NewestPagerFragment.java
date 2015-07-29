package sun.tianyu.ijob.controllers.newest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.LiveQuery;
import com.couchbase.lite.Manager;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.android.AndroidContext;

import java.util.ArrayList;
import java.util.Map;

import sun.tianyu.ijob.IjobApplication;
import sun.tianyu.ijob.R;
import sun.tianyu.ijob.common.CommonFragment;
import sun.tianyu.ijob.common.DefautValues;
import sun.tianyu.ijob.controllers.detail.OfferInforActivity;

/**
 * Created by Developer on 15/07/22.
 */
public class NewestPagerFragment extends CommonFragment implements SwipeRefreshLayout.OnRefreshListener{
    public static final String CategoryNumberKey = "CategoryNum";
    SwipeRefreshLayout swipeLayout;
    ListView listView;
    NewestPagerListAdapter mListAdapter;
    private int categoryNum;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.newest_pager_fragment, container, false);
        Bundle args = getArguments();
//        ((TextView) rootView.findViewById(R.id.text1)).setText(
//                Integer.toString(args.getInt(CategoryNumberKey)));
        categoryNum = args.getInt(CategoryNumberKey, 0);

        listView = (ListView)rootView.findViewById(R.id.newest_list);
//        String[] members = { "スポット1", "スポット2", "スポット3", "スポット4",
//                "スポット5", "スポット6", "スポット7",  "スポット8",  "スポット9",  "スポット10",  "スポット11" };
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_expandable_list_item_1, members);
//        listView.setAdapter(adapter);

        LiveQuery query = getQuery(getDatabase(), categoryNum).toLiveQuery();
        mListAdapter = new NewestPagerListAdapter(getActivity(), query);

        listView.setAdapter(mListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Document doc = (Document) mListAdapter.getItem(i);
                String docID = doc.getId();

                Intent intent = new Intent();
                intent.setClass(getActivity(), OfferInforActivity.class );
                Bundle bundle = new Bundle();
                bundle.putString("document_id", docID);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        swipeLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return rootView;
    }

    private Query getQuery(Database database, final int category_num) {
        com.couchbase.lite.View view = database.getView("jobs"+category_num);
        if (view.getMap() == null) {
            Mapper map = new Mapper() {
                @Override
                public void map(Map<String, Object> document, Emitter emitter) {
                    if (category_num == 0) {
                        if (!"0".equals(document.get("offer_type"))) {
                            java.util.List<Object> keys = new ArrayList<Object>();
                            keys.add(document.get("offer_info"));
                            keys.add(document.get("offer_term"));
                            keys.add(document.get("offer_name"));
                            keys.add(document.get("created_at"));
                            emitter.emit(keys, document);
                        }
                    }else {
                        if ((String.valueOf(category_num)).equals(document.get("offer_type"))) {
                            java.util.List<Object> keys = new ArrayList<Object>();
                            keys.add(document.get("offer_info"));
                            keys.add(document.get("offer_term"));
                            keys.add(document.get("offer_name"));
                            keys.add(document.get("created_at"));
                            emitter.emit(keys, document);
                        }
                    }
                }
            };
            view.setMap(map, "1");
        }

        Query query = view.createQuery();
        query.setDescending(true);// Order the key

        QueryEnumerator rows = null;
        try {
            rows = query.run();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
            Log.e("STYLOG", " Exception: "+e);
        }

        if (rows.getCount() == 0) {
            Log.e("STYLOG", "Data null");
        }

        int rowsCount = rows.getCount();
        for (int i=0; i<rowsCount; i++) {
            QueryRow row = rows.getRow(i);
            Document doc = row.getDocument();
            Log.e("STYLOG", " NewestPagerFragment getQuery:"+ " i: " + i + "  Doc:" + String.valueOf(doc.getProperties()));
        }

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
