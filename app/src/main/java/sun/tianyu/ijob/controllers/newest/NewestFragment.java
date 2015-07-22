package sun.tianyu.ijob.controllers.newest;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import sun.tianyu.ijob.HomeActivity;
import sun.tianyu.ijob.R;
import sun.tianyu.ijob.common.CommonFragment;

/**
 * Created by Developer on 15/07/22.
 */
public class NewestFragment extends CommonFragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

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
        TextView tv = (TextView)rootView.findViewById(R.id.newest_lable);
        tv.setText("1");
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((HomeActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }

}


