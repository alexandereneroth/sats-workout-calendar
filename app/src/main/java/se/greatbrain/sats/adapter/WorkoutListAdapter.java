package se.greatbrain.sats.adapter;

import android.app.Activity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.greatbrain.sats.ActivityType;
import se.greatbrain.sats.ListGroup;
import se.greatbrain.sats.R;

public class WorkoutListAdapter extends BaseAdapter implements StickyListHeadersAdapter
{
    private static final String TAG_LOG = "WorkoutListAdapter";

    private static final int VIEW_TYPE_BOOKED_CLASS = 0;
    private static final int VIEW_TYPE_BOOKED_PRIVATE = 1;
    private static final int VIEW_TYPE_COMPLETED = 2;

    private static final int NUMBER_OF_VIEWS_SERVED_BY_ADAPTER = 4;

    private final SparseArray<ListGroup> groups;
    private final Activity activity;
    private final LayoutInflater inflater;

    private final int numberOfPositions;
    private final Map<Integer, Integer> positionToGroupMappings = new HashMap<>();
    private final Map<Integer, ActivityType> positionToItemMappings = new HashMap<>();

    public WorkoutListAdapter(Activity activity, SparseArray<ListGroup> groups)
    {
        this.groups = groups;
        this.activity = activity;
        inflater = activity.getLayoutInflater();

        int numberOfPositions = 0;
        int itemIndex = 0;
        for (int i = 0; i < groups.size(); ++i)
        {
            ListGroup group = groups.get(i);
            for (int j = 0; j < group.children.size(); ++j)
            {
                positionToGroupMappings.put(itemIndex, i);
                positionToItemMappings.put(itemIndex, group.children.get(j));
                ++itemIndex;
                ++numberOfPositions;
            }
        }
        this.numberOfPositions = numberOfPositions;
    }

    @Override
    public int getCount()
    {
        return numberOfPositions;
    }

    @Override
    public Object getItem(int position)
    {

        return positionToItemMappings.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public long getHeaderId(int position)
    {
        return positionToGroupMappings.get(position);
    }

    @Override
    public int getViewTypeCount()
    {
        return NUMBER_OF_VIEWS_SERVED_BY_ADAPTER;
    }

    @Override
    public int getItemViewType(int position)
    {
        return ((ActivityType) getItem(position)).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        int activityTypeIdOnPosition = getItemViewType(position);
        ActivityType activityTypeOnPosition = ActivityType.getWithId(activityTypeIdOnPosition);

        if (convertView == null)
        {
            convertView = newViewOfType(activityTypeOnPosition, parent);
        }
        switch (activityTypeOnPosition)
        {
            case BookedClass:
                setUpBookedClassView(convertView);
                break;

            case BookedPrivate:
                setUpBookedPrivateView(convertView);
                break;

            case Completed:
                setUpCompletedView(convertView);
                break;
        }
        return convertView;
    }

    private View newViewOfType(ActivityType viewType, ViewGroup parent)
    {
        View convertView = null;

        switch (viewType)
        {
            case BookedClass:
                BookedClassActivityViewHolder bookedClassViewHolder;
                bookedClassViewHolder = new BookedClassActivityViewHolder();
                convertView = inflater.inflate(R.layout.listrow_detail_booked_class, parent,
                        false);
                bookedClassViewHolder.title = ((TextView) convertView.findViewById(R.id
                        .listrow_detail_booked_class_name));
                bookedClassViewHolder.duration = ((TextView) convertView.findViewById(R.id
                        .listrow_detail_booked_class_name));//TODO change and add more

                convertView.setTag(bookedClassViewHolder);
                break;

            case BookedPrivate:
                BookedPrivateActivityViewHolder viewHolder;
                viewHolder = new BookedPrivateActivityViewHolder();
                convertView = inflater.inflate(R.layout.listrow_detail_booked_private, parent,
                        false);
                viewHolder.title = ((TextView) convertView.findViewById(R.id
                        .booked_private_activity_title));
                viewHolder.duration = ((TextView) convertView.findViewById(R.id
                        .booked_private_activity_length));//TODO refactor length to date

                convertView.setTag(viewHolder);
                break;

            case Completed:
                CompletedActivityViewHolder completedActivityViewHolder;
                completedActivityViewHolder = new CompletedActivityViewHolder();
                convertView = inflater.inflate(R.layout.listrow_detail_completed, parent,
                        false);
                completedActivityViewHolder.title = ((TextView) convertView.findViewById(R.id
                        .listrow_detail_completed_class_name));
                completedActivityViewHolder.date = ((TextView) convertView.findViewById(R.id
                        .listrow_detail_completed_class_date));//TODO add more?

                convertView.setTag(completedActivityViewHolder);
                break;
            default:
                throw new IllegalArgumentException(
                        "The passed view type, " + viewType + ", is invalid");
        }

        return convertView;
    }

    private void setUpBookedClassView(View convertView)
    {
        BookedClassActivityViewHolder bookedClassActivityViewHolder =
                (BookedClassActivityViewHolder) convertView.getTag();

        bookedClassActivityViewHolder.title.setText("FISTK");
        bookedClassActivityViewHolder.duration.setText("DURKT");
    }

    private void setUpBookedPrivateView(View convertView)
    {
        BookedPrivateActivityViewHolder bookedPrivateActivityViewHolder =
                (BookedPrivateActivityViewHolder) convertView.getTag();

        String dummyBookedPrivateTitle = "Zebrakastning";
        int dummyDuration = 45;
        bookedPrivateActivityViewHolder.title.setText(dummyBookedPrivateTitle);
    }

    private void setUpCompletedView(View convertView)
    {
        CompletedActivityViewHolder completedActivityViewHolder =
                (CompletedActivityViewHolder) convertView.getTag();

        String dummyCompletedTitle = "Löpning 10m";
        String dummyDate = "2014-04-01";
        completedActivityViewHolder.title.setText(dummyCompletedTitle);
        completedActivityViewHolder.date.setText(dummyDate);
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

    class HeaderViewHolder
    {
        TextView text;
    }

    class BookedPrivateActivityViewHolder
    {
        TextView title;
        TextView duration;
    }

    //TODO
    class BookedClassActivityViewHolder
    {
        TextView title;
        TextView duration;
    }

    class CompletedActivityViewHolder
    {
        TextView title;
        TextView date;
    }
}
