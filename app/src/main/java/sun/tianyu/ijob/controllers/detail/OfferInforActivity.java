package sun.tianyu.ijob.controllers.detail;

import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
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
        setActionBar();
        setContentView(R.layout.offer_info);
        gestureDetectorCompat = new GestureDetectorCompat(this, new OfferInforGestureListener());


        Bundle bundle = getIntent().getExtras();
        String docID = bundle.getString("document_id", "");

        TextView tv = (TextView) findViewById(R.id.u2_title);
        Document doc = ((IjobApplication) getApplication()).database.getDocument(docID);

        String scrolltext = "";
        for (int i = 0; i < 10; i++) {
            scrolltext = scrolltext + String.valueOf(doc.getProperties());
        }
        tv.setText("DocID:" + docID + "\n" + String.valueOf(doc.getProperties()) + " \n" + "   Scroll:" + scrolltext);


    }

    // ActionBar [back]button
    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.offer_info_title));

    }

    // メニューが選択された時の処理
    public boolean onOptionsItemSelected(MenuItem item) {
        // addしたときのIDで識別
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return true;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        this.gestureDetectorCompat.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    class OfferInforGestureListener extends GestureDetector.SimpleOnGestureListener {
        //handle 'swipe left' action only

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
