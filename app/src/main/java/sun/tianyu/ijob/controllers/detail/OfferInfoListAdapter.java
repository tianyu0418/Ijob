package sun.tianyu.ijob.controllers.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.couchbase.lite.Document;

import java.util.ArrayList;
import java.util.List;

import sun.tianyu.ijob.R;

/**
 * Created by Developer on 15/07/29.
 */
public class OfferInfoListAdapter extends BaseAdapter {

    Document document;
    LayoutInflater layoutInflater = null;
    List<List<String>> infoContents;

    public OfferInfoListAdapter(Context context, Document doc) {
        this.document = doc;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        infoContents = new ArrayList<>();
        if (doc.getProperty("offer_info") != null && !"".equals(doc.getProperty("offer_info").toString())) {
            List<String> infoContentsItem = new ArrayList<String>();
            infoContentsItem.add("求人概要");
            infoContentsItem.add(doc.getProperty("offer_info").toString());
            infoContents.add(infoContentsItem);
        }

        if (doc.getProperty("offer_term") != null && !"".equals(doc.getProperty("offer_term").toString())) {
            List<String> infoContentsItem = new ArrayList<String>();
            infoContentsItem.add("作業期間");
            infoContentsItem.add(doc.getProperty("offer_term").toString());
            infoContents.add(infoContentsItem);
        }

        if (doc.getProperty("offer_id") != null && !"".equals(doc.getProperty("offer_id").toString())) {
            List<String> infoContentsItem = new ArrayList<String>();
            infoContentsItem.add("求人ID");
            infoContentsItem.add(doc.getProperty("offer_id").toString());
            infoContents.add(infoContentsItem);
        }
    }

    @Override
    public int getCount() {
        return infoContents.size();
    }

    @Override
    public Object getItem(int i) {
        return infoContents.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.offer_info_list_item, parent, false);

        TextView info_title = ((TextView)convertView.findViewById(R.id.u4_info_item_title));
        TextView info_detail = ((TextView)convertView.findViewById(R.id.u4_info_item_detail));

        info_title.setText(infoContents.get(position).get(0) + ":");
        info_detail.setText(infoContents.get(position).get(1));

        return convertView;

    }
}
