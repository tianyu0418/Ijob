package sun.tianyu.ijob;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.Reducer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sun.tianyu.ijob.common.CommonActivity;
import sun.tianyu.ijob.controllers.newest.NewestFragment;


public class HomeActivity extends CommonActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private String TAG = "STYLOG";
    private IjobApplication application;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (IjobApplication)getApplication();
        if (application.defautValues.PRIVATE) {
            finish();
        }

        setContentView(R.layout.activity_home);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        helloCBL();
    }

    boolean doubleBackToExitPressedOnce;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "[戻る]をもう一度押すと終了", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void helloCBL() {
        // Test Code
        outputAllDocs(((IjobApplication) getApplication()).database);
    }

    // Test Code
    private String createDocument(Database database) {
        // Create a new document and add data
        Document document = database.createDocument();
        String documentId = document.getId();

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = GregorianCalendar.getInstance();
        String currentTimeString = dateFormatter.format(calendar.getTime());

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("offer_id", "4");
        map.put("offer_name", ".NET開発");
        map.put("offer_type", "4");
        map.put("created_at", currentTimeString);
        map.put("offer_info", ".NET経験3年以上。人数2名");
        map.put("offer_term", "2015年8月 ~ 2016年4月");


        try {
            // Save the properties to the document
            document.putProperties(map);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }
        return documentId;
    }

    private void outputAllDocs (Database database) {
        // Query all document
        Query query = database.createAllDocumentsQuery();
//        query.setAllDocsMode(Query.AllDocsMode.ONLY_CONFLICTS);
        QueryEnumerator rows = null;
        try {
            rows = query.run();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
            Log.e("STYLOG", " Exception: "+e);
        }

        if (rows.getCount() == 0) {
            return;
        }
        Log.e("STYLOG", " Current DB Docs Count: "+ rows.getCount());

        int rowsCount = rows.getCount();
        for (int i=0; i<rowsCount; i++) {
            QueryRow row = rows.getRow(i);
            Document doc = row.getDocument();
            Log.e("STYLOG", " Current DB Docs:"+ " i: " + i + "  Doc:" + String.valueOf(doc.getProperties()));
        }

        com.couchbase.lite.View phoneView = database.getView("rmb");
        if (phoneView.getMap() == null) {
            Mapper map = new Mapper() {
                @Override
                public void map(Map<String, Object> document, Emitter emitter) {
                    java.util.List<String> offerTypes = new ArrayList<>();
                    offerTypes.add(String.valueOf(document.get("offer_type")));
                    emitter.emit(offerTypes, document);
                }
            };
            Reducer reduce = new Reducer() {
                @Override
                public Object reduce(List<Object> keys, List<Object> values, boolean rereduce) {
                    for (int i = 0; i < keys.size(); i++) {
                        if (keys.lastIndexOf(keys.get(i)) == i) {
                            return keys.lastIndexOf(i);
                        }

                    }
                    return null;
                }
            };
            phoneView.setMapReduce(map, reduce, "2");

        }



//        phoneView.setMapReduce(new Mapper() {
//            @Override
//            public void map(Map<String, Object> document, Emitter emitter) {
//                java.util.List<String> offerTypes = new ArrayList<>();
//                offerTypes.add(String.valueOf(document.get("offer_type")));
//                emitter.emit(offerTypes, document);
//            }
//        }, new Reducer() {
//            @Override
//            public Object reduce(List<Object> keys, List<Object> values, boolean rereduce) {
//                for(int i = 0; i < keys.size(); i++) {
//                    if (keys.lastIndexOf(keys.get(i)) == i) {
//                        return keys.lastIndexOf(i);
//                    }
//
//                }
//
//                return null;
//            }
//
//        }, "2");

    }

    private void outputContents (Database database, String docID) {
        Document retrievedDocument = database.getDocument(docID);
        Log.e(TAG, "retrievedDocument=" + String.valueOf(retrievedDocument.getProperties()));
    }

    // Test Code
    private void updateDoc(Database database, String documentId) {
        Document document = database.getDocument(documentId);
        try {
            // Update the document with more data
            Map<String, Object> updatedProperties = new HashMap<String, Object>();
            updatedProperties.putAll(document.getProperties());
            updatedProperties.put("eventDescription", "Everyone is invited!");
            updatedProperties.put("address", "123 Elm St.");
            document.putProperties(updatedProperties);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        switch (position) {
            case 0:
                FragmentManager newestFragment = getSupportFragmentManager();
                newestFragment.beginTransaction()
                        .replace(R.id.container, NewestFragment.newInstance(position + 1))
                        .commit();
                break;

            default:
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                        .commit();
                break;
        }

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_newest);
                break;
            case 2:
                mTitle = getString(R.string.title_search);
                break;
            case 3:
                mTitle = getString(R.string.title_bookmark);
                break;
            case 4:
                mTitle = getString(R.string.title_look_history);
                break;
            case 5:
                mTitle = getString(R.string.title_apply_history);
                break;
            case 6:
                mTitle = getString(R.string.title_log_in);
                break;
            default:
                mTitle = "";
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    public void setTabs() {
        ActionBar actionBar = getSupportActionBar();

        actionBar.removeAllTabs();
        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create a tab listener that is called when the user changes tabs.
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                // show the given tab
            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // hide the given tab
            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // probably ignore this event
            }
        };

        // Add 3 tabs, specifying the tab's text and TabListener
        for (int i = 0; i < 15; i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText("Tab " + (i + 1))
                            .setTabListener(tabListener));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            // Section
            getMenuInflater().inflate(R.menu.home, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((HomeActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER, 0));
        }
    }

}
