package se.greatbrain.sats.adapter;

import android.app.Activity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import se.greatbrain.sats.ListGroup;
import se.greatbrain.sats.R;

public class WorkoutListAdapter extends BaseExpandableListAdapter{

    private final SparseArray<ListGroup> groups;
    private final Activity activity;
    private final LayoutInflater inflater;

    public WorkoutListAdapter(Activity activity, SparseArray<ListGroup> groups)
    {
        this.groups = groups;
        this.activity = activity;
        inflater = activity.getLayoutInflater();
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).children.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).children.get(childPosition);
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i2) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.listrow_group, null);
        }

        ListGroup listGroup = (ListGroup) getGroup(groupPosition);
        ((CheckedTextView) convertView).setText(listGroup.title);
        ((CheckedTextView) convertView).setChecked(true);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final String child = (String) getChild(groupPosition, childPosition);
        TextView text = null;

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.listrow_detail, null);
        }

        text = (TextView) convertView.findViewById(R.id.listrow_detail_title);
        text.setText(child);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return false;
    }
}
