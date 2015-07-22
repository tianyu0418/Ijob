package sun.tianyu.ijob.controllers.newest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

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
        // Our object is just an integer :-P
        args.putInt(NewestPagerFragment.CategoryNumberKey, i + 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Category " + (position + 1);
    }
}
