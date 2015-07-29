package sun.tianyu.ijob.controllers.newest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import sun.tianyu.ijob.R;

import com.couchbase.lite.Attachment;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Document;
import com.couchbase.lite.LiveQuery;
import com.couchbase.lite.SavedRevision;

import sun.tianyu.ijob.common.ImageHelper;
import sun.tianyu.ijob.common.LiveQueryAdapter;

/**
 * Created by Developer on 15/07/23.
 */
public class NewestPagerListAdapter extends LiveQueryAdapter {
    private static final int THUMBNAIL_SIZE_PX = 150;


    public NewestPagerListAdapter(Context context, LiveQuery query) {
        super(context, query);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.newest_pager_list_item, null);
        }

        final Document job = (Document) getItem(position);
//        Log.e("STYLOG","NewestPagerListAdapter  document::: " + String.valueOf(job.getProperties()));

        if (job == null || job.getCurrentRevision() == null) {
            return convertView;
        }

        Bitmap thumbnail = null;
        java.util.List<Attachment> attachments = job.getCurrentRevision().getAttachments();
        if (attachments != null && attachments.size() > 0) {
            Attachment attachment = attachments.get(0);
            try {
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(attachment.getContent(), null, options);
                options.inSampleSize = ImageHelper.calculateInSampleSize(
                        options, THUMBNAIL_SIZE_PX, THUMBNAIL_SIZE_PX);
                attachment.getContent().close();

                // Need to get a new attachment again as the FileInputStream
                // doesn't support mark and reset.
                attachments = job.getCurrentRevision().getAttachments();
                attachment = attachments.get(0);
                options.inJustDecodeBounds = false;
                Bitmap bitmap = BitmapFactory.decodeStream(attachment.getContent(), null, options);
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();
                thumbnail = ThumbnailUtils.extractThumbnail(bitmap, THUMBNAIL_SIZE_PX, THUMBNAIL_SIZE_PX);
                attachment.getContent().close();
            } catch (Exception e) {
                Log.e("STYLOG", "Cannot decode the attached image", e);
            }
        }



        TextView titletext = (TextView) convertView.findViewById(R.id.u1_title);
        titletext.setText(String.valueOf(job.getProperty("offer_name")));
        TextView infoText = (TextView) convertView.findViewById(R.id.u1_info);
        infoText.setText(String.valueOf(job.getProperty("offer_info") != null ? job.getProperty("offer_info") : ""));

            /*
            If there are conflicting revisions, show a conflict icon.
            getConflictingRevisions always returns the current revision
            so we must check for size > 1.
             */
        java.util.List<SavedRevision> conflicts = null;
        try {
            conflicts = job.getConflictingRevisions();
        } catch (CouchbaseLiteException e) {

        }

        return convertView;
    }
}
