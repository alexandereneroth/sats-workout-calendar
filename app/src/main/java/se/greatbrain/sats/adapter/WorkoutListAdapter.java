package se.greatbrain.sats.adapter;

import android.app.Activity;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

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
    private final int numberOfNonHeadingItems;
    private final Map<Integer, Integer> positionToGroupMappings = new HashMap<>();
    private final Map<Integer, String> positionToItemMappings = new HashMap<>();

    public WorkoutListAdapter(Activity activity, SparseArray<ListGroup> groups)
    {
        this.groups = groups;
        this.activity = activity;
        countries = activity.getApplicationContext().getResources().getStringArray(R
                .array.countries);
        inflater = activity.getLayoutInflater();

        int numberOfNonHeadingItems = 0;
        int itemIndex = 0;
        for (int i = 0; i < groups.size(); ++i)
        {
            ListGroup group = groups.get(i);
            for (int j = 0; j < group.children.size(); ++j)
            {
                positionToGroupMappings.put(itemIndex, i);
                positionToItemMappings.put(itemIndex, group.children.get(i));
                ++itemIndex;
                ++numberOfNonHeadingItems;
            }
        }
        this.numberOfNonHeadingItems = numberOfNonHeadingItems;
    }

    @Override
    public int getCount()
    {
        return numberOfNonHeadingItems;//countries.length;
    }

    @Override
    public Object getItem(int position)
    {

        return positionToItemMappings.get(position);//countries[position];
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;

        if (convertView == null)
        {
            holder = new ViewHolder();

            // Chose between the different listrow types here
            switch(position % 3)
            {
                case 0:
                    convertView = inflater.inflate(R.layout.listrow_detail_booked_class, parent, false);
                    holder.text = (TextView) convertView.findViewById(R.id
                            .listrow_detail_booked_class_title);
                    holder.text.setText(positionToItemMappings.get(position));
                    break;
                case 1:
                    convertView = inflater.inflate(R.layout.listrow_detail_booked_private, parent, false);
                    holder.text = (TextView) convertView.findViewById(R.id
                            .listrow_detail_booked_private_title);
                    holder.text.setText(positionToItemMappings.get(position));
                    break;
                case 2:
                    convertView = inflater.inflate(R.layout.listrow_detail_completed, parent, false);
//                    holder.text = (TextView) convertView.findViewById(R.id
//                            .listrow_detail_completed_title);
//                    holder.text.setText(positionToItemMappings.get(position));
                    break;
            }
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }


        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent)
    {
        HeaderViewHolder holder;
        if (convertView == null)
        {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.listrow_group, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.listrow_group_title);
            convertView.setTag(holder);
        }
        else
        {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        int groupNumber = positionToGroupMappings.get(position);

        String headerText = groups.get(groupNumber).title;
        holder.text.setText(headerText);
        return convertView;
    }

    @Override
    public long getHeaderId(int position)
    {
        //return the first character of the country as ID because this is what headers are based
        // upon
//        return 0;
        Log.d(TAG_LOG, "position: " + position);
        Log.d(TAG_LOG, "countries: " + countries);
        return positionToGroupMappings.get(
                position); //countries[position].subSequence(0, 1).charAt(0);
    }

    class HeaderViewHolder
    {
        TextView text;
    }

    class ViewHolder
    {
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
