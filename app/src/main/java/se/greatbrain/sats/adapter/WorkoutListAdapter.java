package se.greatbrain.sats.adapter;

import android.app.Activity;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.greatbrain.sats.ListGroup;
import se.greatbrain.sats.R;

public class WorkoutListAdapter extends BaseAdapter implements StickyListHeadersAdapter
{
    private static final String TAG_LOG = "WorkoutListAdapter";
    private final SparseArray<ListGroup> groups;
    private final Activity activity;
    private final LayoutInflater inflater;

    private String[] countries;

    public WorkoutListAdapter(Activity activity, SparseArray<ListGroup> groups) {
        this.groups = groups;
        this.activity = activity;
        countries = activity.getApplicationContext().getResources().getStringArray(R
                .array.countries);
//        inflater = LayoutInflater.from(activity.getApplicationContext());
        inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return countries.length;
    }

    @Override
    public Object getItem(int position) {
        return countries[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.listrow_detail, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.listrow_detail_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText(countries[position]);

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.listrow_group, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.listrow_group_title);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        //set header text as first char in name
        String headerText = "" + countries[position].subSequence(0, 1).charAt(0);
        holder.text.setText(headerText);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        //return the first character of the country as ID because this is what headers are based upon
//        return 0;
        Log.d(TAG_LOG, "position: " + position);
        Log.d(TAG_LOG, "countries: " + countries);
        return countries[position].subSequence(0, 1).charAt(0);
    }

    class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder {
        TextView text;
    }


//    @Override
//    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
//            ViewGroup parent)
//    {
//        if (convertView == null)
//        {
//            convertView = inflater.inflate(R.layout.listrow_group, null);
//        }
//
//        ListGroup listGroup = (ListGroup) getGroup(groupPosition);
//        ((CheckedTextView) convertView).setText(listGroup.title);
//        ((CheckedTextView) convertView).setChecked(true);
//
//        return convertView;
//    }
//
//    @Override
//    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
//            View convertView, ViewGroup parent)
//    {
//
//        final String child = (String) getChild(groupPosition, childPosition);
//        TextView text = null;
//
//        if (convertView == null)
//        {
//            convertView = inflater.inflate(R.layout.listrow_detail, null);
//        }
//
//        text = (TextView) convertView.findViewById(R.id.listrow_detail_title);
//        text.setText(child);
//
//        return convertView;
//    }
}
