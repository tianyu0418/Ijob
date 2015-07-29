package sun.tianyu.ijob.controllers.detail;

import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.couchbase.lite.Document;

import sun.tianyu.ijob.IjobApplication;
import sun.tianyu.ijob.R;
import sun.tianyu.ijob.common.CommonActivity;

/**
 * Created by Developer on 15/07/27.
 */
public class OfferInforActivity extends CommonActivity {

    private GestureDetectorCompat gestureDetectorCompat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offer_info);
        gestureDetectorCompat = new GestureDetectorCompat(this, new OfferInforGestureListener());

        setTitle(getResources().getString(R.string.offer_info_title));

        // ドキュメントID取得
        Bundle bundle = getIntent().getExtras();
        String docID = bundle.getString("document_id", "");

        // 表示ドキュメント取得
        Document doc = ((IjobApplication) getApplication()).database.getDocument(docID);

        ListView infoList = (ListView) findViewById(R.id.u2_info_list);


        // リストビューアダプターを設置
//        String[] members = { doc.getProperty("offer_name").toString(), doc.getProperty("offer_term").toString(), doc.getProperty("offer_info").toString(), doc.getProperties().toString()};
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplication(),
//                android.R.layout.simple_expandable_list_item_1, members);
        OfferInfoListAdapter adapter = new OfferInfoListAdapter(getApplication(), doc);

        infoList.setDividerHeight(0);
        infoList.setAdapter(adapter);

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        this.gestureDetectorCompat.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    class OfferInforGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {

         /*
         Toast.makeText(getBaseContext(),
          event1.toString() + "\n\n" +event2.toString(),
          Toast.LENGTH_SHORT).show();
         */
            if (event2.getX() > event1.getX() + 400.0) {
                Toast.makeText(getBaseContext(),
                        "Swipe left - startActivity()",
                        Toast.LENGTH_SHORT).show();

                //switch another activity

                finish();
            }

            return true;
        }
    }


}
