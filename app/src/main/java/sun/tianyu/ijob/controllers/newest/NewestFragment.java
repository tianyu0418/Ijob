package sun.tianyu.ijob.controllers.newest;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;
import android.widget.TextView;
import sun.tianyu.ijob.HomeActivity;
import sun.tianyu.ijob.R;
import sun.tianyu.ijob.common.CommonFragment;
import sun.tianyu.ijob.common.adview.AdView;

/**
 * Created by Developer on 15/07/22.
 */
public class NewestFragment extends CommonFragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    ViewPager newestPager;
    NewestPagerAdapter newestPagerAdapter;
    TabHost tabHost;
    HorizontalScrollView tabScrollView;
    private final String TAG_NAME_NEWEST_ALL = "allCategory";


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static NewestFragment newInstance(int sectionNumber) {
        NewestFragment fragment = new NewestFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public NewestFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.newest_fragment, container, false);
        newestPagerAdapter = new NewestPagerAdapter(getChildFragmentManager());
        newestPager = (ViewPager) rootView.findViewById(R.id.newest_pager);
        newestPager.setAdapter(newestPagerAdapter);
        setTab(rootView);
        AdView adView = new AdView(getActivity(), rootView);
        adView.setHelpBannerHidden(false);
        return rootView;
    }

    public void setTab(View rootView) {
        tabHost = (TabHost) rootView.findViewById(android.R.id.tabhost);
        tabScrollView = (HorizontalScrollView) rootView.findViewById(R.id.tab_scroll);
        tabHost.setup();

        for (int i = 0; i < newestPagerAdapter.getCount(); i++) {
            tabHost.addTab(tabHost
                    .newTabSpec(String.valueOf(i))
                    .setIndicator(newestPagerAdapter.getPageTitle(i))
                    .setContent(android.R.id.tabcontent));
        }

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                newestPager.setCurrentItem(Integer.valueOf(tabId));
            }
        });

        newestPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabHost.setCurrentTab(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                View tabView = tabHost.getTabWidget().getChildAt(position);
                if (tabView != null) {
                    final int width = tabScrollView.getWidth();
                    int scrollPos = tabView.getLeft() - (width - tabView.getWidth()) / 2;
                    if (tabView.getLeft() > width / 2) {
                        scrollPos = tabView.getLeft();
                    }
                    tabScrollView.scrollTo(scrollPos, 0);

                } else {
                    tabScrollView.scrollBy(positionOffsetPixels, 0);
                }
            }
        });

    }

    //共通スタイル設定
    public void setButtonStyle(TextView tv) {
        // Tab高さ
        final float scale = getResources().getDisplayMetrics().density;
        int tab_height = (int) (48 * scale + 0.5f);
        tv.setGravity(Gravity.CENTER);
        tv.setSingleLine(true);
        tv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tv.setHeight(tab_height);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setTextColor(Color.WHITE);
//        tv.setBackgroundResource(R.drawable.tab_select);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((HomeActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));

    }

}


