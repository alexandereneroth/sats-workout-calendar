package se.greatbrain.sats.fragment;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.greenrobot.event.EventBus;
import se.greatbrain.sats.adapter.CalendarPagerAdapter;
import se.greatbrain.sats.event.ScrollEvent;
import se.greatbrain.sats.view.CalendarRowView;
import se.greatbrain.sats.R;

public class CalendarColumnFragment extends Fragment
{
    public static final int PAST_WEEK = -1;
    public static final int THIS_WEEK = 0;
    public static final int UPCOMING_WEEK = 1;

    private static final String TAG = "ScreenSlidePageFragment";

    private float calendarHeight;
    private int numActivities;
    private int pointInTime;
    private int previousWeeksActivities;
    private int nextWeeksActivities;
    private boolean shouldDrawLineToNextWeek;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        Resources resources = getResources();

        calendarHeight = resources.getDimension(R.dimen.calendar_height);

        LinearLayout rootView = (LinearLayout) inflater.inflate(R.layout.fragment_calendar_column,
                container, false);

        Bundle args = getArguments();

        final int indexInAdapter = args.getInt(CalendarPagerAdapter.ADAPTER_POSITION);
        numActivities = args.getInt(CalendarPagerAdapter.NUMBER_OF_ACTIVITIES);
        pointInTime = args.getInt(CalendarPagerAdapter.POINT_IN_TIME);

        if (indexInAdapter % 2 == 0)
        {
            rootView.setBackgroundColor(resources.getColor(R.color.primary_calendar));
        }
        else
        {
            rootView.setBackgroundColor(resources.getColor(R.color.secondary_calendar));
        }

        rootView.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                EventBus.getDefault().post(new ScrollEvent(indexInAdapter));
            }
        });

        addTopRow(rootView);
        addRows(rootView);
        addDateRow(rootView, args.getString(CalendarPagerAdapter.DATE_STRING));

        return rootView;
    }

    private void addTopRow(LinearLayout rootView)
    {
        View topRow = new View(rootView.getContext());
        topRow.setBackground(getResources().getDrawable(R.drawable.calendar_toprow_background));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, getHeightOfOneRow(CalendarPagerAdapter.NUM_ROWS));
        rootView.addView(topRow, params);
    }

    private void addRows(LinearLayout rootView)
    {
        boolean weekHasMoreActivitiesThanRows = false;

        if (numActivities > CalendarPagerAdapter.NUM_ROWS)
        {
            weekHasMoreActivitiesThanRows = true;
        }

        // Start at row 'NUM_ROWS' and add all rows through 0, the zero row is extra and is not part
        // of the number of rows
        for (int rowIndex = CalendarPagerAdapter.NUM_ROWS; rowIndex > -1; --rowIndex)
        {
            boolean isZeroRow = (rowIndex == 0);

            CalendarRowView.Builder rowBuilder = new CalendarRowView
                    .Builder(rootView.getContext(), numActivities);
            if (shouldDrawCircleOnThisRow(rowIndex))
            {
                int circleType = hasPastActivity() ?
                        CalendarRowView.PAST_ACTIVITY : CalendarRowView.FUTURE_OR_CURRENT_ACTIVITY;

                rowBuilder.drawCircle(circleType);
                rowBuilder.drawLineToPreviousWeek(previousWeeksActivities);
                if(shouldDrawLineToNextWeek)
                {
                    rowBuilder.drawLineToNextWeek(nextWeeksActivities);
                }
            }
            if (isZeroRow)
            {
                rowBuilder.setIsZeroRow();
            }

            CalendarRowView row = rowBuilder.build();

            row.setText((weekHasMoreActivitiesThanRows ? "+" : "") + String.valueOf(numActivities));
            row.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

            int rowFullHeight = getHeightOfOneRow(CalendarPagerAdapter.NUM_ROWS);
            int rowHeight = isZeroRow ? rowFullHeight / 2 : rowFullHeight;

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, rowHeight);

            rootView.addView(row, params);
        }
    }

    private void addDateRow(LinearLayout rootView, String date)
    {
        TextView row = new TextView(rootView.getContext());
        row.setText(date);
        row.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        row.setBackground(getResources().getDrawable(R.drawable.calendar_column_row_date_bg));
        row.setPadding(0, 7, 0, 0);
        row.setGravity(Gravity.CENTER | Gravity.TOP);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, getHeightOfOneRow(CalendarPagerAdapter.NUM_ROWS));

        rootView.addView(row, params);
    }

    private int getHeightOfOneRow(int rows)
    {
        return (int) (calendarHeight / (rows + 2.5));
    }

    private boolean shouldDrawCircleOnThisRow(int rowIndex)
    {
        return (rowIndex == numActivities) || (rowIndex < numActivities && rowIndex ==
                CalendarPagerAdapter.NUM_ROWS);
    }

    private boolean hasPastActivity()
    {
        return pointInTime == PAST_WEEK;
    }

}