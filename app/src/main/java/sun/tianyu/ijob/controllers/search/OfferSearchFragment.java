package sun.tianyu.ijob.controllers.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import sun.tianyu.ijob.HomeActivity;
import sun.tianyu.ijob.R;
import sun.tianyu.ijob.common.CommonFragment;

/**
 * Created by Developer on 15/07/28.
 */
public class OfferSearchFragment extends CommonFragment{
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static OfferSearchFragment newInstance(int sectionNumber) {
        OfferSearchFragment fragment = new OfferSearchFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public OfferSearchFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.offer_search, container, false);

        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                return false;
            }
        });

        /* */

        /*　検索ボックス */
        EditText editText = (EditText)rootView.findViewById(R.id.u3_offer_search_box);
        editText.setHint("任意ワードを入力してください。");
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(rootView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        editText.setOnKeyListener(new View.OnKeyListener(){
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_ENTER) {
                    String searchword = ((EditText)v).getText().toString();
                    if (searchword != null && searchword.length() != 0) {
                        // ここに処理
                        final InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                        Toast.makeText(getActivity(), "検索開始: " + searchword, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), OfferSearchResultActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("search_word", searchword);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else{
                        //キーワード入力されていない場合
                        Toast.makeText(getActivity(), "キーワードを入力してください。", Toast.LENGTH_SHORT).show();

                    }
                    return true;
                }
                return false;
            }
        });


        return rootView;


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((HomeActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER, 0));
    }
}
