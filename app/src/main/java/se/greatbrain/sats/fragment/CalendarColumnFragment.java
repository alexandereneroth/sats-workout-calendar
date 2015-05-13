package se.greatbrain.sats.fragment;

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
import se.greatbrain.sats.adapter.CalendarPagerAdapter;
import se.greatbrain.sats.event.CalendarColumnClickedEvent;
import se.greatbrain.sats.util.DateUtil;
import se.greatbrain.sats.util.VerticalLayouter;
import se.greatbrain.sats.util.PixelUtil;
import se.greatbrain.sats.view.CalendarRowView;

public class CalendarColumnFragment extends Fragment
{
    public static final int PAST_WEEK = -1;

    private static final String TAG = "ScreenSlidePageFragment";

    private float calendarHeight;
    private int numActivities;
    private int pointInTime;
    private int previousWeeksActivities;
    private int nextWeeksActivities;
    private boolean shouldDrawLineToNextWeek;
    private boolean hasMoreActivitiesThanAvailibleRows = false;

    private int screenWidth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        final Resources resources = getResources();

        calendarHeight = resources.getDimension(R.dimen.calendar_height);
        screenWidth = PixelUtil.getScreenDimensions(container.getContext()).x;

        if (numActivities > CalendarPagerAdapter.numRows)
        {
            hasMoreActivitiesThanAvailibleRows = true;
        }

        RelativeLayout rootView = (RelativeLayout) inflater.inflate(
                R.layout.fragment_calendar_column,
                container, false);

        final Bundle args = getArguments();

        final int indexInAdapter = args.getInt(CalendarPagerAdapter.ADAPTER_POSITION);
        numActivities = args.getInt(CalendarPagerAdapter.NUMBER_OF_ACTIVITIES);
        pointInTime = args.getInt(CalendarPagerAdapter.POINT_IN_TIME);
        nextWeeksActivities = args.getInt(CalendarPagerAdapter.NEXT_NUMBER_OF_ACTIVITIES);
        previousWeeksActivities = args.getInt(CalendarPagerAdapter.PREVIOUS_NUMBER_OF_ACTIVITIES);
        shouldDrawLineToNextWeek = args.getBoolean(CalendarPagerAdapter.HAS_NEXT_WEEK_PASSED);

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

        int rowHeight = getHeightOfOneRow();

        new VerticalLayouter(rootView)
                .setViewHeight(rowHeight)
                .addView(getTopRow(rootView))
                .addViews(getNormalRows(rootView))
                .setViewHeight(rowHeight / 2)
                .addView(getZeroRow(rootView))
                .setViewHeight(rowHeight)
                .addView(getDateRow(rootView, args.getString(CalendarPagerAdapter.DATE_STRING)));

        return rootView;
    }

    private View getTopRow(RelativeLayout rootView)
    {
        View topRow = new View(rootView.getContext());

        if (pointInTime == DateUtil.CURRENT_WEEK)
        {
            topRow.setBackground(getTopRowBackground());
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
        // Start at row 'NUM_ROWS' and add all rows through 0, the zero row is extra and is not part
        // of the number of rows
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
        row = setUpRowBase(row, rowIndex);

        row.setText(
                (hasMoreActivitiesThanAvailibleRows ? "+" : "") + String.valueOf(numActivities));
        row.setBackground(getResources().getDrawable(R.drawable.line));
        return row;
    }

    private View getZeroRow(ViewGroup rootView)
    {
        CalendarRowView row = CalendarRowView.newZeroRowInstance(rootView.getContext());
        row = setUpRowBase(row, 0);

        return row;
    }

    private CalendarRowView setUpRowBase(CalendarRowView rowView, int rowIndex)
    {
        if (shouldDrawCircleOnThisRow(rowIndex))
        {
            final int circleType = columnIsForAPastWeek() ?
                    CalendarRowView.PAST_ACTIVITY : CalendarRowView.FUTURE_OR_CURRENT_ACTIVITY;

            rowView.setCircle(circleType);
            rowView.setDrawLineToPreviousWeek(previousWeeksActivities);
            rowView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

            if (shouldDrawLineToNextWeek)
            {
                rowView.setDrawLineToNextWeek(nextWeeksActivities);
            }
        }
        return rowView;
    }

    private View getDateRow(RelativeLayout rootView, String date)
    {
        TextView row = new TextView(rootView.getContext());
        row.setText(date);
        row.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        row.setBackground(getResources().getDrawable(R.drawable.calendar_column_row_date_bg));
        row.setPadding(0, 7, 0, 0);
        row.setGravity(Gravity.CENTER | Gravity.TOP);

        return row;
    }

    private LayerDrawable getTopRowBackground()
    {
        LayerDrawable layerList = (LayerDrawable) getResources().getDrawable(
                R.drawable.calendar_marker_layer_list);
        final int topInset = (int) Math.round(getHeightOfOneRow() / 2.7);
        final int sideInset = (int) Math.round(getWidth() / 2.6);
        layerList.setLayerInset(1, sideInset, topInset, sideInset, 0);

        return layerList;
    }

    private int getHeightOfOneRow()
    {
        return (int) Math.round(calendarHeight / (CalendarPagerAdapter.numRows + 2.5));
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