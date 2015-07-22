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
    ArrayList<String> linkUrlArray= new ArrayList<>();
    //ヘルプバナーフラグ
    public static boolean helpbannerFlg = false;
    //初期化処理
    private void init(Context cx) {
        context = cx;
    }
    private static boolean isHelpbannerFlg = false;

    /**
     * 初期化
     */
    public HelpBanner(Context cx) {
        init(cx);
        Activity parentActivity = (Activity)cx;
        helpBannerImage = (ViewFlow) parentActivity.findViewById(R.id.helpbannerImage);
        mFlowIndicator = (CircleFlowIndicator) parentActivity.findViewById(R.id.viewflowindic);
        mFlowIndicator.setVisibility(View.INVISIBLE);
        mIndicatorParent = (FrameLayout) parentActivity.findViewById(R.id.indicatorparent);
        boolean isHelpBannerTap = false;

        if(SHOWHELPBANNER == 1){
            helpBannerImage.setVisibility(View.VISIBLE);
            //li add start
            setUpBanner();
            initBanner(imageUrlList);
            //li add end
        }
    }

    /**
     * 初期化
     */
    public HelpBanner(Context cx, View rootView) {
        init(cx);
        helpBannerImage = (ViewFlow) rootView.findViewById(R.id.helpbannerImage);
        mFlowIndicator = (CircleFlowIndicator) rootView.findViewById(R.id.viewflowindic);
        mFlowIndicator.setVisibility(View.INVISIBLE);
        mIndicatorParent = (FrameLayout) rootView.findViewById(R.id.indicatorparent);
        AppDataAdmin.isHelpBannerTap = false;

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
    private void setUpBanner(){
        List<BannerData> banners = AppDataAdmin.bannerDatas;
        if(banners!=null){
            for(int i=0;i<banners.size();i++){
                BannerData banner= banners.get(i);
                imageUrlList.add(banner.imageUrl);
                linkUrlArray.add(banner.linkUrl);
            }
        }else{
            imageUrlList.add(AppDataAdmin.myAdViewImageURL);
            String tempUrl = AppDataAdmin.myAdViewLinkURL + "?" + "OS_NAME=" + "Android" + "&" + "OS_VER="+
                    AppDataAdmin.androidVersion.replace(".", "") + "&" + "APP_VER="
                    +Constants.STAGE_AND_DEMO_APPLICATION_VERSION.replace(".", "");
            linkUrlArray.add(tempUrl);
        }
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
            if(AppDataAdmin.bannerIndicatorFlg){
                helpBannerImage.setFlowIndicator(mFlowIndicator);
            }
            helpBannerImage.setSelection(0);
            helpBannerImage.setTimeSpan(AppDataAdmin.bannerScrollInterval);
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
        if(AppDataAdmin.bannerIndicatorFlg){
            if(imageUrlList.size()>1) {
                //バナー一枚以上
                mFlowIndicator.setVisibility(View.VISIBLE);
            }else{
                //バナー一枚の場合非表示
                mFlowIndicator.setVisibility(View.INVISIBLE);
            }
        }else{
            //システム設定より非表示
            mFlowIndicator.setVisibility(View.INVISIBLE);
        }
    }
}
