package sun.tianyu.ijob.controllers.search;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import sun.tianyu.ijob.common.CommonActivity;

/**
 * Created by Developer on 15/07/28.
 */
public class OfferSearchResultActivity extends CommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String searchWrod = getIntent().getExtras().getString("search_word", "-333");
        Toast.makeText(this, " 検索結果 : " + searchWrod, Toast.LENGTH_SHORT).show();

        // TODO: DB検索
        // TODO: UI設置 - 結果リスト　　結果から詳細へ行く






    }
}
