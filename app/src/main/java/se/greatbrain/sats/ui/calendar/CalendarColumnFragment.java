package se.greatbrain.sats.ui.calendar;

import android.content.res.Resources;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.greenrobot.event.EventBus;
import se.greatbrain.sats.R;
import se.greatbrain.sats.event.CalendarColumnClickedEvent;
import se.greatbrain.sats.util.DateUtil;
import se.greatbrain.sats.util.DimensionUtil;
import se.greatbrain.sats.util.VerticalLayouter;

public class CalendarColumnFragment extends Fragment
{
    public static final int PAST_WEEK = -1;
    private static final String TAG = "ScreenSlidePageFragment";

    private int numActivities;
    private int numActivities_previousWeek;
    private int numActivities_nextWeek;
    //negative is before this week, 0 is this week, positive is after this week
    private int pointInTime;
    // last week shouldn't draw a line to this week (because this week is a non-past week)
    private boolean shouldDrawLineToNextWeek;
    private String bottomRowDateString;
    private boolean weekHasMoreActivitiesThanRowsAvailable = false;

    private int screenWidth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        final Resources resources = getResources();

        screenWidth = DimensionUtil.getScreenDimensions(container.getContext()).x;

        final Bundle args = getArguments();
        numActivities = args.getInt(CalendarPagerAdapter.NUMBER_OF_ACTIVITIES);
        pointInTime = args.getInt(CalendarPagerAdapter.POINT_IN_TIME);
        numActivities_nextWeek = args.getInt(CalendarPagerAdapter.NEXT_NUMBER_OF_ACTIVITIES);
        numActivities_previousWeek = args.getInt(CalendarPagerAdapter.PREVIOUS_NUMBER_OF_ACTIVITIES);
        shouldDrawLineToNextWeek = args.getBoolean(CalendarPagerAdapter.HAS_NEXT_WEEK_PASSED);
        bottomRowDateString = args.getString(CalendarPagerAdapter.DATE_STRING);
        final int indexInAdapter = args.getInt(CalendarPagerAdapter.ADAPTER_POSITION);

        if (numActivities > CalendarPagerAdapter.numRows)
        {
            weekHasMoreActivitiesThanRowsAvailable = true;
        }

        RelativeLayout rootView = (RelativeLayout) inflater.inflate(
                R.layout.fragment_calendar_column, container, false);

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
                EventBus.getDefault().post(new CalendarColumnClickedEvent(indexInAdapter));
            }
        });

        final int rowHeight = CalendarFragment.getHeightOfOneRow();

        VerticalLayouter.layoutIn(rootView)
                .useViewHeight(rowHeight)
                .placeView(getTopRow(rootView))
                .placeViews(getNormalRows(rootView))
                .useViewHeight(rowHeight / 2)
                .placeView(getZeroRow(rootView))
                .useViewHeight(rowHeight)
                .placeView(getDateRow(rootView));

        return rootView;
    }

    private View getTopRow(RelativeLayout rootView)
    {
        View topRow = new View(rootView.getContext());

        if (pointInTime == DateUtil.CURRENT_WEEK)
        {
            topRow.setBackground(getTopRowBackgroundWithCurrentWeekMarker());
        }
        else
        {
            topRow.setBackground(getResources().getDrawable(R.drawable.calendar_toprow_background));
        }
        return topRow;
    }

    private View[] getNormalRows(RelativeLayout rootView)
    {
        View[] rows = new View[CalendarPagerAdapter.numRows];
        // Loops backwards so rowIndex can be used as the CalendarRowView's text (the number in the
        // circle)
        int i = 0;
        for (int rowIndex = CalendarPagerAdapter.numRows; rowIndex > 0; --rowIndex, ++i)
        {
            rows[i] = getNormalRow(rootView, rowIndex);
        }
        return rows;
    }

    private View getNormalRow(RelativeLayout rootView, int rowIndex)
    {
        CalendarRowView row = CalendarRowView.newNormalRowInstance(rootView.getContext(), rowIndex);
        row = setUpCalendarRowViewBase(row, rowIndex);

        row.setText(
                (weekHasMoreActivitiesThanRowsAvailable ? "+" : "") + String.valueOf(numActivities));
        row.setBackground(getResources().getDrawable(R.drawable.line));
        return row;
    }

    private View getZeroRow(ViewGroup rootView)
    {
        CalendarRowView row = CalendarRowView.newZeroRowInstance(rootView.getContext());
        row = setUpCalendarRowViewBase(row, 0);

        return row;
    }

    private CalendarRowView setUpCalendarRowViewBase(CalendarRowView rowView, int rowIndex)
    {
        if (shouldDrawCircleOnThisRow(rowIndex))
        {
            final int circleType = columnIsForAPastWeek() ?
                    CalendarRowView.PAST_ACTIVITY : CalendarRowView.FUTURE_OR_CURRENT_ACTIVITY;

            rowView.setCircle(circleType);
            rowView.setDrawLineToPreviousWeek(numActivities_previousWeek);
            rowView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

            if (shouldDrawLineToNextWeek)
            {
                rowView.setDrawLineToNextWeek(numActivities_nextWeek);
            }
        }
        return rowView;
    }

    private View getDateRow(RelativeLayout rootView)
    {
        TextView row = new TextView(rootView.getContext());
        row.setText(bottomRowDateString);
        row.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        row.setBackground(getResources().getDrawable(R.drawable.calendar_column_row_date_bg));
        row.setPadding(0, 7, 0, 0);
        row.setGravity(Gravity.CENTER | Gravity.TOP);

        return row;
    }

    private LayerDrawable getTopRowBackgroundWithCurrentWeekMarker()
    {
        LayerDrawable layerList = (LayerDrawable) getResources().getDrawable(
                R.drawable.calendar_marker_layer_list);
        final int topInset = (int) Math.round(CalendarFragment.getHeightOfOneRow() / 2.7);
        final int sideInset = (int) Math.round(getWidth() / 2.6);
        layerList.setLayerInset(1, sideInset, topInset, sideInset, 0);

        return layerList;
    }

    private int getWidth()
    {
        return screenWidth / CalendarFragment.NUM_SIMULTANEOUS_PAGES;
    }

    private boolean shouldDrawCircleOnThisRow(int rowIndex)
    {
        return (rowIndex == numActivities) || (rowIndex < numActivities && rowIndex ==
                CalendarPagerAdapter.numRows);
    }

    private boolean columnIsForAPastWeek()
    {
        return pointInTime == PAST_WEEK;
    }
}