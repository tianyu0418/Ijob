package sun.tianyu.ijob.controllers.newest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import sun.tianyu.ijob.R;

/**
 * Created by Developer on 15/07/22.
 */
public class NewestPagerAdapter extends FragmentStatePagerAdapter {
    public NewestPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new NewestPagerFragment();
        Bundle args = new Bundle();
        args.putInt(NewestPagerFragment.CategoryNumberKey, i);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String pageTitle = "";
        switch (position) {
            case 0:
                pageTitle = "すべて";
                break;
            case 1:
                pageTitle = "Java";
                break;
            case 2:
                pageTitle = "iOS/Android";
                break;
            case 3:
                pageTitle = "Web";
                break;
            case 4:
                pageTitle = ".NET";
                break;
            default:
                pageTitle = "すべて";
                break;
        }
        return pageTitle;

    }
}
