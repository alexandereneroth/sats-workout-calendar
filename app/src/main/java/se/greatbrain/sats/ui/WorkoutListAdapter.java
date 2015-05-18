package se.greatbrain.sats.ui;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.greenrobot.event.EventBus;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.greatbrain.sats.R;
import se.greatbrain.sats.data.ActivityWrapper;
import se.greatbrain.sats.data.TimeOfDay;
import se.greatbrain.sats.data.model.TrainingActivity;
import se.greatbrain.sats.event.ClassDetailEvent;
import se.greatbrain.sats.util.DateUtil;

public class WorkoutListAdapter extends BaseAdapter implements StickyListHeadersAdapter
{
    private static final String TAG = "WorkoutListAdapter";

    private static final int NUMBER_OF_VIEW_TYPES_SERVED_BY_ADAPTER = 4;

    private final List<ActivityWrapper> listItems;
    private final int numberOfListItems;
    private int numberOfPastListItems;
    private final Map<Integer, Integer> listItemPositionToWeek = new HashMap<>();
    private final Map<Integer, Integer> weekHashToItemPosition = new HashMap<>();
    private final Activity activity;

    private final LayoutInflater inflater;

    public WorkoutListAdapter(Activity activity, List<ActivityWrapper> listItems)
    {
        this.inflater = activity.getLayoutInflater();
        this.listItems = listItems;
        this.activity = activity;

        numberOfListItems = listItems.size();

        for (int i = 0; i < listItems.size(); i++)
        {
            ActivityWrapper activityWrapper = listItems.get(i);
            int weekHash = (activityWrapper.year * 100) + activityWrapper.week;
            listItemPositionToWeek.put(i, weekHash);

            //So week hash is mapped with the FIRST item under that week
            if (weekHashToItemPosition.get(weekHash) == null)
            {
                weekHashToItemPosition.put(weekHash, i);
            }

            // Counts number of past list items so we know what position today is
            if (DateUtil.dateHasPassed(activityWrapper.trainingActivity.getDate()))
            {
                numberOfPastListItems++;
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ActivityWrapper activityWrapper = ((ActivityWrapper) getItem(position));
        if (convertView == null)
        {
            if (activityWrapper.isPastOrCompleted())
            {
                convertView = inflatePastOrCompletedActivityView(parent);
            }
            else
            {
                if (activityWrapper.activityType == ActivityWrapper.GROUP)
                {
                    convertView = inflateGroupActivityView(parent);
                }
                else // trainingActivity type is private
                {
                    convertView = inflatePrivateActivityView(parent);
                }
            }
        }
        if (activityWrapper.isPastOrCompleted())
        {
            setUpPastOrCompletedActivityView(convertView, position);
        }
        else
        {
            if (activityWrapper.activityType == ActivityWrapper.GROUP)
            {
                setUpGroupActivityView(convertView, position);
            }
            else // trainingActivity type is private
            {
                setUpPrivateActivityView(convertView, position);
            }
        }

        return convertView;
    }

    /**
     * View inflation
     */

    private View inflatePastOrCompletedActivityView(ViewGroup parent)
    {
        View inflateMe = inflater.inflate(R.layout.listrow_detail_completed, parent,
                false);

        PastOrCompletedActivityViewHolder viewHolder = new PastOrCompletedActivityViewHolder();
        viewHolder.image = ((ImageView) inflateMe.findViewById(R.id
                .listrow_detail_completed_class_training_type_picture));
        viewHolder.title = ((TextView) inflateMe.findViewById(R.id
                .listrow_detail_completed_class_name));
        viewHolder.date = ((TextView) inflateMe.findViewById(R.id
                .listrow_detail_completed_class_date));
        viewHolder.comment = ((TextView) inflateMe.findViewById(R.id
                .listrow_detail_completed_class_comment));
        viewHolder.completed = ((TextView) inflateMe.findViewById(R.id
                .listrow_detail_completed_class_completed));
        viewHolder.checkbox = ((ImageView) inflateMe.findViewById(R.id
                .listrow_detail_completed_class_completed_checkbox));

        inflateMe.setTag(viewHolder);

        return inflateMe;
    }

    private View inflateGroupActivityView(ViewGroup parent)
    {
        View inflateMe = inflater.inflate(R.layout.listrow_detail_booked_class, parent,
                false);

        GroupActivityViewHolder viewHolder = new GroupActivityViewHolder();
        viewHolder.title = ((TextView) inflateMe.findViewById(R.id
                .listrow_detail_booked_class_name));
        viewHolder.duration = ((TextView) inflateMe.findViewById(R.id
                .listrow_detail_booked_class_duration_minutes));
        viewHolder.location = ((TextView) inflateMe.findViewById(R.id
                .listrow_detail_booked_class_location));
        viewHolder.instructor = ((TextView) inflateMe.findViewById(R.id
                .listrow_detail_booked_class_instructor_name));
        viewHolder.positionInQueue = ((TextView) inflateMe.findViewById(R.id
                .listrow_detail_booked_class_person_queue));
        viewHolder.timeHours = ((TextView) inflateMe.findViewById(R.id
                .listrow_detail_booked_class_time_hours));
        viewHolder.timeMinutes = ((TextView) inflateMe.findViewById(R.id
                .listrow_detail_booked_class_time_minutes));
        viewHolder.aboutTrainingButton = ((TextView) inflateMe.findViewById(R.id
                .about_the_class_textview));
        inflateMe.setTag(viewHolder);

        return inflateMe;
    }

    private View inflatePrivateActivityView(ViewGroup parent)
    {
        View inflateMe = inflater.inflate(R.layout.listrow_detail_booked_private, parent,
                false);

        PrivateActivityViewHolder viewHolder = new PrivateActivityViewHolder();
        viewHolder.title = ((TextView) inflateMe.findViewById(R.id
                .listrow_detail_booked_private_name));
        viewHolder.duration = ((TextView) inflateMe.findViewById(R.id
                .listrow_detail_booked_private_duration));
        viewHolder.comment = ((TextView) inflateMe.findViewById(R.id
                .listrow_detail_booked_private_comment));

        inflateMe.setTag(viewHolder);

        return inflateMe;
    }

    /**
     * View setup
     */

    private void setUpPastOrCompletedActivityView(View convertView, int position)
    {
        ActivityWrapper activityWrapper = (ActivityWrapper) getItem(position);
        TrainingActivity trainingActivity = activityWrapper.trainingActivity;

        int trainingTypePictureId = getTrainingTypePictureDrawable(trainingActivity);
        String title = getListRowTitle(trainingActivity);
        String date = DateUtil.getPastOrCompletedActivityDate(trainingActivity.getDate());
        String comment = getCommentString(trainingActivity.getComment());
        String completed = activityWrapper.isCompleted ? "Avklarad!" : "Avklarad?";
        int checkboxId = activityWrapper.isCompleted ? R.drawable.done_icon : R.drawable
                .done_2_icon;

        PastOrCompletedActivityViewHolder pastOrCompletedActivityViewHolder =
                (PastOrCompletedActivityViewHolder) convertView.getTag();
        pastOrCompletedActivityViewHolder.image.setImageResource(trainingTypePictureId);
        pastOrCompletedActivityViewHolder.title.setText(title);
        pastOrCompletedActivityViewHolder.date.setText(date);
        pastOrCompletedActivityViewHolder.comment.setText(comment);
        pastOrCompletedActivityViewHolder.completed.setText(completed);
        pastOrCompletedActivityViewHolder.checkbox.setImageResource(checkboxId);
    }

    private void setUpGroupActivityView(View convertView, final int position)
    {
        ActivityWrapper activityWrapper = (ActivityWrapper) getItem(position);
        TrainingActivity trainingActivity = activityWrapper.trainingActivity;

        String title = getListRowTitle(trainingActivity);
        String duration = String.valueOf(trainingActivity.getDurationInMinutes() + " min");
        String location = trainingActivity.getCenter().getName();
        String instructor = trainingActivity.getBooking().getSatsClass().getInstructorId();
        TimeOfDay timeOfDay = DateUtil.getTimeOfDayFromDate(trainingActivity.getDate());
        String timeHours = timeOfDay.getHourString();
        String timeMinutes = timeOfDay.getMinuteString();
        String positionInQueue = String.valueOf(trainingActivity.getBooking().getPositionInQueue());

        GroupActivityViewHolder groupActivityViewHolder =
                (GroupActivityViewHolder) convertView.getTag();
        groupActivityViewHolder.aboutTrainingButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ActivityWrapper wrapper = listItems.get(position);
                EventBus.getDefault().postSticky(new ClassDetailEvent(wrapper));

                Intent intent = new Intent(activity, ClassDetailActivity.class);
                activity.startActivity(intent);
            }
        });
        groupActivityViewHolder.title.setText(title);
        groupActivityViewHolder.duration.setText(duration);
        groupActivityViewHolder.location.setText(location);
        groupActivityViewHolder.instructor.setText(instructor);
        groupActivityViewHolder.timeHours.setText(timeHours);
        groupActivityViewHolder.timeMinutes.setText(timeMinutes);
        groupActivityViewHolder.positionInQueue.setText(positionInQueue);
        if (trainingActivity.getBooking().getPositionInQueue() == 0)
        {
            groupActivityViewHolder.positionInQueue.setVisibility(View.GONE);
//            groupActivityViewHolder.queueIcon.setVisibility(View.GONE);
        }
    }

    private void setUpPrivateActivityView(View convertView, int position)
    {
        ActivityWrapper activityWrapper = (ActivityWrapper) getItem(position);
        TrainingActivity trainingActivity = activityWrapper.trainingActivity;

        String title = getListRowTitle(trainingActivity);
        String duration = trainingActivity.getDurationInMinutes() + " min";
        String comment = getCommentString(trainingActivity.getComment());

        PrivateActivityViewHolder privateActivityViewHolder =
                (PrivateActivityViewHolder) convertView.getTag();
        privateActivityViewHolder.title.setText(title);
        privateActivityViewHolder.duration.setText(duration);
        privateActivityViewHolder.comment.setText(comment);
    }

    /**
     * Below are view holders that contain all the different views that will be set in the
     * different layouts.
     * <p/>
     * These are used like this:
     * <code>bookedClassActivityViewHolder.title.setText("Some text");</code>
     * Because it is faster than using findViewById, like this:
     * <code>((TextField)view.findViewById(R.id.listrow_detail_booked_class_name)).setText("Some
     * text");</code>
     */

    class HeaderViewHolder
    {
        TextView text;
    }

    class PastOrCompletedActivityViewHolder
    {
        TextView title;
        TextView date;
        TextView comment;
        TextView completed;
        ImageView checkbox;
        ImageView image;
    }

    class GroupActivityViewHolder
    {
        TextView title;
        TextView duration;
        TextView aboutTrainingButton;
        TextView location;
        TextView instructor;
        TextView positionInQueue;
        TextView timeHours;
        TextView timeMinutes;
    }

    class PrivateActivityViewHolder
    {
        TextView title;
        TextView duration;
        TextView comment;
    }

    /**
     * Overrides required by BaseAdapter
     */

    @Override
    public int getCount()
    {
        return numberOfListItems;
    }

    @Override
    public Object getItem(int position)
    {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    /**
     * Overrides required by StickyListHeaders
     */

    @Override
    public long getHeaderId(int position)
    {
        final ActivityWrapper itemOnPosition = (ActivityWrapper) getItem(position);
        final boolean theDate_OfTheItemOnThisPosition_hasPassed =
                DateUtil.dateHasPassed(itemOnPosition.trainingActivity.getDate());
        if (theDate_OfTheItemOnThisPosition_hasPassed)
        {
            // This way, every item before today will get the same header id if they belong to the
            // same week.
            return listItemPositionToWeek.get(position);
        }
        else
        {
            // This way, every item from today onwards will get its own header id.
            return position;
        }
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

        String headerText = getListTitle(position, listItems.get(position));
        holder.text.setText(headerText);
        return convertView;
    }

    private String getListTitle(int position, ActivityWrapper activityWrapper)
    {
        if (activityWrapper.isPastOrCompleted())
        {
            return DateUtil.getListTitleCompleted(activityWrapper.trainingActivity.getDate());
        }
        else
        {
            return DateUtil.getListTitlePlanned(activityWrapper.trainingActivity.getDate());
        }
    }

    /**
     * Overrides required in lists containing views of multiple different layouts
     */

    @Override
    public int getViewTypeCount()
    {
        return NUMBER_OF_VIEW_TYPES_SERVED_BY_ADAPTER;
    }

    @Override
    public int getItemViewType(int position)
    {
        ActivityWrapper activityWrapper = (ActivityWrapper) getItem(position);

        final int pastOrCompletedViewId = 0;
        final int groupActivityViewId = 1;
        final int privateActivityViewId = 2;

        if (activityWrapper.isPastOrCompleted())
        {
            return pastOrCompletedViewId;
        }
        else if (activityWrapper.activityType == ActivityWrapper.GROUP)
        {
            return groupActivityViewId;
        }
        else if (activityWrapper.activityType == ActivityWrapper.PRIVATE)
        {
            return privateActivityViewId;
        }
        else
        {
            throw new IllegalStateException("A list item has an invalid state in its " +
                    "ActivityWrapper!");
        }
    }

    /**
     * Public accessors
     */

    public int positionOfTodaysFirstItem()
    {
        return numberOfPastListItems;
    }

    public int getPositionFromWeekHash(int weekHash)
    {
        if (weekHashToItemPosition.containsKey(weekHash))
        {
            return weekHashToItemPosition.get(weekHash);
        }
        else
        {
            return getClosestPositionToWeekHash(weekHash);
        }
    }

    /**
     * Private helper methods
     */

    private int getClosestPositionToWeekHash(int weekHash)
    {
        TreeMap<Integer, Integer> sortedWeekHashToItemPosition = new TreeMap<>();
        sortedWeekHashToItemPosition.putAll(weekHashToItemPosition);

        for (int i : sortedWeekHashToItemPosition.keySet())
        {
            if (i > weekHash)
            {
                return weekHashToItemPosition.get(i);
            }
        }

        return listItems.size() - 1;
    }

    private int getTrainingTypePictureDrawable(TrainingActivity trainingActivity)
    {
        String trainingActivityType = trainingActivity.getType().toLowerCase();

        if (trainingActivityType.equals("gym"))
        {
            return R.drawable.strength_trainging_icon;
        }

        String trainingActivitySubType = trainingActivity.getSubType().toLowerCase();

        if (trainingActivitySubType.contains("cycl"))
        {
            return R.drawable.cykling_icon;
        }
        if (trainingActivitySubType.equals("walking") || trainingActivitySubType.equals
                ("running"))
        {
            return R.drawable.running_icon;
        }
        if (trainingActivityType.equals("group"))
        {
            return R.drawable.group_training_icon;
        }
        return R.drawable.all_training_icons;
    }

    /**
     * Returns the title for a list row view
     *
     * @param trainingActivity The training activity which to base the title on
     * @return Returns the title for a list row view
     */
    private String getListRowTitle(TrainingActivity trainingActivity)
    {
        String title;
        if (trainingActivity.getActivityType() == null)
        {
            title = trainingActivity.getSubType();
        }
        else
        {
            title = trainingActivity.getActivityType().getName();
        }
        return title;
    }

    private String getCommentString(String comment)
    {
        if (comment.isEmpty())
        {
            return comment = "Lägg till kommentar";
        }
        else
        {
            return comment = "1 kommentar";
        }
    }
}
