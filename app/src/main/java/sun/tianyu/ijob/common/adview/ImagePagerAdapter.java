package sun.tianyu.ijob.common.adview;

/**
 * Created by Developer on 15/07/23.
 */
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sun.tianyu.ijob.IjobApplication;
import sun.tianyu.ijob.R;

public class ImagePagerAdapter extends BaseAdapter {

    private Context context;
    private List<String> imageIdList;
    private List<String> linkUrlArray;
    private int size;
    private boolean isInfiniteLoop;
    private String HELPBANNERLINKURL = "loadUrl";
    private String WHEREFROM = "wherefrom";
    private String adImageUrl;
    private String LOG_REMARK_URL;

    public ImagePagerAdapter(Context context, List<String> imageIdList,
                             List<String> urllist) {
        this.context = context;
        this.imageIdList = imageIdList;
        if (imageIdList != null) {
            this.size = imageIdList.size();
        }
        this.linkUrlArray = urllist;
        //this.urlTitlesList = urlTitlesList;
        isInfiniteLoop = false;

    }

    @Override
    public int getCount() {
        // Infinite loop
        return isInfiniteLoop ? Integer.MAX_VALUE : imageIdList.size();
    }

    /**
     * get really position
     *
     * @param position
     * @return
     */
    private int getPosition(int position) {
        return isInfiniteLoop ? position % size : position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup container) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = holder.imageView = new ImageView(context);
            holder.imageView
                    .setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
            holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        int imgPos = ImagePagerAdapter.this.getPosition(position);
        if(imageIdList.size()-1 < imgPos){
            imgPos = 0;
        }
        adImageUrl = imageIdList.get(imgPos);

        Bitmap mBanner = ((IjobApplication) context.getApplicationContext()).mImageCache.getBitmap(adImageUrl);
        if (mBanner != null) {
            holder.imageView.setBackgroundDrawable(new BitmapDrawable(mBanner));
        } else {
            ImageRequest re;
            final String imageUrl = adImageUrl;
            final ImageView imageView = holder.imageView;
            re = new ImageRequest(imageUrl,
                    new Response.Listener<Bitmap>(){
                        @Override
                        public void onResponse(Bitmap arg0) {
                            BitmapDrawable bd = new BitmapDrawable(arg0);
                            imageView.setBackgroundDrawable(bd);
                            if (null != arg0) {
                                ((IjobApplication) context.getApplicationContext()).mImageCache.putBitmap(imageUrl,arg0);
                            }
                        }
                    }, 0, 0, null,
                    new Response.ErrorListener(){

                        @Override
                        public void onErrorResponse(VolleyError arg0) {
                            imageView.setBackgroundResource(R.drawable.appicon);

                        }

                    });

            ((IjobApplication) context.getApplicationContext()).getRequestQueue().add(re);
        }
        //li add end
        holder.imageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String url = linkUrlArray.get(ImagePagerAdapter.this.getPosition(position));

                    Intent intent = new Intent(context, AdWebActivity.class);
                    intent.putExtra(WHEREFROM, 1);
                    // ヘルプURLにパラメータを追加
                    intent.putExtra(HELPBANNERLINKURL,url);
                    context.startActivity(intent);

            }
        });

        return view;
    }

    private static class ViewHolder {

        ImageView imageView;
    }

    /**
     * @return the isInfiniteLoop
     */
    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    /**
     * @param isInfiniteLoop
     *            the isInfiniteLoop to set
     */
    public ImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;


        return this;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

}
