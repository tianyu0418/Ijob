package sun.tianyu.ijob.common;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import sun.tianyu.ijob.R;

/**
 * Created by Developer on 15/07/22.
 */
public class CommonActivity extends ActionBarActivity {
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar();
    }
    // ActionBar [back]button
    private void setActionBar() {
        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
    }

    public void setTitle(String title) {
        actionBar.setTitle(title);

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




}
