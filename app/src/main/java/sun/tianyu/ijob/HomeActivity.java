package sun.tianyu.ijob;

import android.app.Activity;
import android.app.Application;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.couchbase.lite.Attachment;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.LiveQuery;
import com.couchbase.lite.Manager;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.SavedRevision;
import com.couchbase.lite.UnsavedRevision;
import com.couchbase.lite.android.AndroidContext;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import sun.tianyu.ijob.common.GlobalValues;


public class HomeActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private String DB_NAME = "ijob_db_guest";
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

    private void helloCBL() {
        Manager manager = null;
        Database database = null;
        try {
            manager = new Manager(new AndroidContext(this), Manager.DEFAULT_OPTIONS);
            database = manager.getDatabase(DB_NAME);
        } catch (Exception e) {
            Log.e(TAG, "Error getting database", e);
            return;
        }
        // Create the document
        String documentId = createDocument(database);
    /* Get and output the contents */
        outputContents(database, documentId);

    /* Update the document and add an attachment */
        updateDoc(database, documentId);
        // Add an attachment
        addAttachment(database, documentId);
//    /* Get and output the contents with the attachment */
//        outputContentsWithAttachment(database, documentId);


        // Query all document
        Query query = database.createAllDocumentsQuery();
        /*The important thing that is set the docs mode*/
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
        Log.e("STYLOG", " Current DB Docment Count: "+ rows.getCount());

        int rowsCount = rows.getCount();
        for (int i=0; i<rowsCount; i++) {
            QueryRow row = rows.getRow(i);
            Document doc = row.getDocument();
//            Log.e("STYLOG", " Current DB Docs:"+ " i: " + i + "  Doc:" + String.valueOf(doc.getProperties()));
        }

        for (Iterator<QueryRow> it = rows; it.hasNext(); ) {
            QueryRow row = it.next();
            Log.e("STYLOG", "Widget named: "+ row.getKey() + "" + row.getValue());
        }

    }

    private String createDocument(Database database) {
        // Create a new document and add data
        Document document = database.createDocument();
        String documentId = document.getId();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("offer_id", "001");
        map.put("offer_name", "Android Developer");

        try {
            // Save the properties to the document
            document.putProperties(map);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }
        return documentId;
    }

    private void outputContents (Database database, String docID) {
        Document retrievedDocument = database.getDocument(docID);
        Log.e(TAG, "retrievedDocument=" + String.valueOf(retrievedDocument.getProperties()));
    }

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

    private void addAttachment(Database database, String documentId) {
        Document document = database.getDocument(documentId);
        try {
        /* Add an attachment with sample data as POC */
            ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{1, 3, 5, 7});
            UnsavedRevision revision = document.getCurrentRevision().createRevision();
            revision.setAttachment("binaryData", "application/octet-stream", inputStream); //MIME type inputStream);
        /* Save doc & attachment to the local DB */
            revision.save();
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }

        //retrieve the attachment
        Document fetchedSameDoc = database.getExistingDocument(documentId);
        SavedRevision saved = fetchedSameDoc.getCurrentRevision();
        // The content of the attachment is a byte[] we created
        Attachment attach = saved.getAttachment("binaryData");
        int i = 0;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(attach.getContent()));
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        StringBuffer values = new StringBuffer();
        while (i++ < 4) {
            // We knew the size of the byte array
            // This is the content of the attachment
            try {
                values.append(reader.read() + " ");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, "The docID: " + documentId + ", attachment contents was: " + values.toString());
    }



    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
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
                mTitle = getString(R.string.title_look_history);
                break;
            case 4:
                mTitle = getString(R.string.title_apply_history);
                break;
            case 5:
                mTitle = getString(R.string.title_log_in);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
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
            ((HomeActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
