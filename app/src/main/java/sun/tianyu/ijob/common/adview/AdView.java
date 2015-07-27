package sun.tianyu.ijob.common.adview;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import sun.tianyu.ijob.R;
import sun.tianyu.ijob.common.DefautValues;

/**
 * Created by Developer on 15/07/22.
 */
public class AdView {
    private  static int SHOWHELPBANNER = DefautValues.ADHidden; //0.バナー非表示　1. 表示
    //バナー主体
    private ViewFlow helpBannerImage;
    //コンテークス
    private Context context;
    //インジゲーター親コンポーネント
    private FrameLayout mIndicatorParent;
    //ログスクリーンID
    public static String LOG_SCREEN_ID;
    //インジゲーター
    private CircleFlowIndicator mFlowIndicator;
    //バナー画像配列
    private ArrayList<String> imageUrlList = new ArrayList<>();
    //リンク先配列
    private ArrayList<String> linkUrlArray= new ArrayList<>();
    //初期化処理
    private void init(Context cx) {
        context = cx;
    }

    /**
     * 初期化
     */
    public AdView(Context cx) {
        init(cx);
        Activity parentActivity = (Activity)cx;
        helpBannerImage = (ViewFlow) parentActivity.findViewById(R.id.helpbannerImage);
        mFlowIndicator = (CircleFlowIndicator) parentActivity.findViewById(R.id.viewflowindic);
        mFlowIndicator.setVisibility(View.INVISIBLE);
        mIndicatorParent = (FrameLayout) parentActivity.findViewById(R.id.indicatorparent);

        if(SHOWHELPBANNER == 1){
            helpBannerImage.setVisibility(View.VISIBLE);
            setUpBanner();
            initBanner(imageUrlList);
        }
    }

    /**
     * 初期化
     */
    public AdView(Context cx, View rootView) {
        init(cx);
        helpBannerImage = (ViewFlow) rootView.findViewById(R.id.helpbannerImage);
        mFlowIndicator = (CircleFlowIndicator) rootView.findViewById(R.id.viewflowindic);
        mFlowIndicator.setVisibility(View.INVISIBLE);
        mIndicatorParent = (FrameLayout) rootView.findViewById(R.id.indicatorparent);

        if(SHOWHELPBANNER == 1){
            helpBannerImage.setVisibility(View.VISIBLE);
            //li add start
            setUpBanner();
            initBanner(imageUrlList);
            //li add end
        }
    }

    /**
     * バナー設定
     */
    private void setUpBanner() {
//        imageUrlList.add("http://static.wixstatic.com/media/da90ae_26b8786811c14c5aa323cf20bb119360.png_srb_p_170_84_75_22_0.50_1.20_0.00_png_srb");
//        linkUrlArray.add("http://www.sundata-service.com/#!about/c4nz");
        imageUrlList.add("https://upload.wikimedia.org/wikipedia/commons/e/ec/Yahoo_Japan_logo.png");
        linkUrlArray.add("http://www.yahoo.co.jp");
        imageUrlList.add("http://www.adsmartonline.com/blog/wp-content/uploads/2013/01/bing.png");
        linkUrlArray.add("http://www.bing.com");


    }
    /**
     * バナー非表示設定
     */
    public void setHelpBannerHidden(boolean hidFlg){
        if(hidFlg){
            helpBannerImage.setVisibility(View.GONE);
            setBannerIndicator(false);
            mIndicatorParent.setVisibility(View.GONE);
        }else{
            if (SHOWHELPBANNER == 1) {
                helpBannerImage.setVisibility(View.VISIBLE);
                setBannerIndicator(true);
            }
        }

    }

    /**
     * True,False判定
     */
    public  boolean checkTrueOrFalse(int arg0){
        return (arg0 == 0);
    }

    /**
     * バナー初期設定
     */
    private void initBanner(ArrayList<String> imageUrlList) {

        helpBannerImage.setAdapter(new ImagePagerAdapter(context, imageUrlList,
                linkUrlArray).setInfiniteLoop(true));
        helpBannerImage.setmSideBuffer(imageUrlList.size());
        helpBannerImage.setFlowIndicator(null);
        setBannerIndicator(true);
        if(imageUrlList.size()>1){
            helpBannerImage.setFlowIndicator(mFlowIndicator);
            helpBannerImage.setSelection(0);
            helpBannerImage.setTimeSpan(5);
            helpBannerImage.startAutoFlowTimer();
        }else{
            helpBannerImage.setCanSwipe(false);
        }
    }

    /**
     * バナーインジゲーター設定
     */
    private void setBannerIndicator(Boolean flag){
        if(!flag){
            //バナー非表示のため
            mFlowIndicator.setVisibility(View.INVISIBLE);
            return;
        }
            if(imageUrlList.size()>1) {
                //バナー一枚以上
                mFlowIndicator.setVisibility(View.VISIBLE);
            }else{
                //バナー一枚の場合非表示
                mFlowIndicator.setVisibility(View.INVISIBLE);
            }
    }
}
