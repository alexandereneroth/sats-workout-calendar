package se.greatbrain.sats.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.greatbrain.sats.Activiteee;
import se.greatbrain.sats.ActivityType;
import se.greatbrain.sats.R;
import se.greatbrain.sats.model.realm.TrainingActivity;

public class WorkoutListAdapter extends BaseAdapter implements StickyListHeadersAdapter
{
    private static final String TAG_LOG = "WorkoutListAdapter";

    private static final int NUMBER_OF_VIEWS_SERVED_BY_ADAPTER = 4;

    private final List<Activiteee> activities;
    private final Activity activity;
    private final LayoutInflater inflater;

    private int numberOfPositions;
    private final Map<Integer, Integer> positionToWeek = new HashMap<>();

    public WorkoutListAdapter(Activity activity, List<Activiteee> activities)
    {
        this.activities = activities;
        this.activity = activity;
        inflater = activity.getLayoutInflater();

        numberOfPositions = activities.size();
        for (int i = 0; i < activities.size(); ++i)
        {
            Activiteee activiteee = activities.get(i);
            int weekHash = (activiteee.year * 100) + activiteee.week;
            positionToWeek.put(i, weekHash);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Activiteee activiteee = ((Activiteee) getItem(position));

        final boolean isCompletedActivityView = activiteee.activityStatus ==
                Activiteee.COMPLETED || activiteee.dateHasPassed();

        if (convertView == null)
        {
            if (isCompletedActivityView)
            {
                convertView = inflateCompletedActivityView(parent);
            }
            else
            {
                if (activiteee.activityType == Activiteee.GROUP)
                {
                    convertView = inflateGroupActivityView(parent);
                }
                else // if activity type is private
                {
                    convertView = inflatePrivateActivityView(parent);
                }
            }
        }
        if (isCompletedActivityView)
        {
            setUpCompletedView(convertView, position);
        }
        else
        {
            if (activiteee.activityType == Activiteee.GROUP)
            {
                setUpBookedClassView(convertView);
            }
            else
            {
                setUpBookedPrivateView(convertView, position);
            }
        }

        return convertView;
    }

    private View inflateCompletedActivityView(ViewGroup parent)
    {
        View newView;

        CompletedActivityViewHolder completedActivityViewHolder;
        completedActivityViewHolder = new CompletedActivityViewHolder();
        newView = inflater.inflate(R.layout.listrow_detail_completed, parent,
                false);
        completedActivityViewHolder.title = ((TextView) newView.findViewById(R.id
                .listrow_detail_completed_class_name));
        completedActivityViewHolder.date = ((TextView) newView.findViewById(R.id
                .listrow_detail_completed_class_date));
        completedActivityViewHolder.comment = ((TextView) newView.findViewById(R.id
                .listrow_detail_completed_class_comment));
        completedActivityViewHolder.completed = ((TextView) newView.findViewById(R.id
                .listrow_detail_completed_class_completed));

        newView.setTag(completedActivityViewHolder);

        return newView;
    }

    private View inflateGroupActivityView(ViewGroup parent)
    {
        View convertView;
        BookedClassActivityViewHolder bookedClassViewHolder;
        bookedClassViewHolder = new BookedClassActivityViewHolder();
        convertView = inflater.inflate(R.layout.listrow_detail_booked_class, parent,
                false);
        bookedClassViewHolder.title = ((TextView) convertView.findViewById(R.id
                .listrow_detail_booked_class_name));
        bookedClassViewHolder.duration = ((TextView) convertView.findViewById(R.id
                .listrow_detail_booked_class_duration_minutes));

        //TODO add more

        convertView.setTag(bookedClassViewHolder);

        return convertView;
    }

    private View inflatePrivateActivityView(ViewGroup parent)
    {
        View convertView;

        BookedPrivateActivityViewHolder viewHolder;
        viewHolder = new BookedPrivateActivityViewHolder();
        convertView = inflater.inflate(R.layout.listrow_detail_booked_private, parent,
                false);

        viewHolder.title = ((TextView) convertView.findViewById(R.id
                .listrow_detail_booked_private_name));
        viewHolder.duration = ((TextView) convertView.findViewById(R.id
                .listrow_detail_booked_private_duration));
        viewHolder.comment = ((TextView) convertView.findViewById(R.id
                .listrow_detail_booked_private_comment));

        convertView.setTag(viewHolder);

        return convertView;
    }

    private void setUpCompletedView(View convertView, int position)
    {
        CompletedActivityViewHolder completedActivityViewHolder =
                (CompletedActivityViewHolder) convertView.getTag();

        Activiteee activiteee = (Activiteee) getItem(position);
        TrainingActivity trainingActivity = activiteee.activity;

        String title = trainingActivity.getSubType();
        String date = trainingActivity.getDate();
        String comment = "Kommentar: " + trainingActivity.getComment();
        String completed = activiteee.activityStatus == Activiteee.COMPLETED ?
                "Avklarad!" : "Avklarad?";
        completedActivityViewHolder.title.setText(title);
        completedActivityViewHolder.date.setText(date);
        completedActivityViewHolder.comment.setText(comment);
        completedActivityViewHolder.completed.setText(completed);
    }

    private void setUpBookedClassView(View convertView)
    {
        BookedClassActivityViewHolder bookedClassActivityViewHolder =
                (BookedClassActivityViewHolder) convertView.getTag();

        bookedClassActivityViewHolder.title.setText("FISTK");
        bookedClassActivityViewHolder.duration.setText("69");
    }

    private void setUpBookedPrivateView(View convertView, int position)
    {
        BookedPrivateActivityViewHolder bookedPrivateActivityViewHolder =
                (BookedPrivateActivityViewHolder) convertView.getTag();

        Activiteee trainingActivity = (Activiteee) getItem(position);
        String title = trainingActivity.activity.getSubType();
        String duration = trainingActivity.activity.getDurationInMinutes() + " min";
        String comment = "Kommentar: " + trainingActivity.activity.getComment();

        bookedPrivateActivityViewHolder.title.setText(title);
        bookedPrivateActivityViewHolder.duration.setText(duration);
        bookedPrivateActivityViewHolder.comment.setText(comment);

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
        int groupNumber = positionToWeek.get(position);

        String headerText = String.valueOf("Year " + activities.get(position).year + " Week " +
                activities.get(position).week);
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
        TextView comment;
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
        TextView comment;
        TextView completed;
    }

    @Override
    public int getCount()
    {
        return numberOfPositions;
    }

    @Override
    public Object getItem(int position)
    {
        return activities.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public long getHeaderId(int position)
    {
        return positionToWeek.get(position);
    }

    @Override
    public int getViewTypeCount()
    {
        return NUMBER_OF_VIEWS_SERVED_BY_ADAPTER;
    }

    @Override
    public int getItemViewType(int position)
    {
        Activiteee activiteee = (Activiteee) getItem(position);
        if (activiteee.activityStatus == Activiteee.COMPLETED)
        {
            return activiteee.COMPLETED;
        }
        else
        {
            return activiteee.activityType;
        }

    }
}
