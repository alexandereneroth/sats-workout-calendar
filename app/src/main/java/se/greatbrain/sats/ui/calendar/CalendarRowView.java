package se.greatbrain.sats.ui.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.widget.TextView;

import se.greatbrain.sats.R;

public abstract class CalendarRowView extends TextView
{
    private static final String TAG = "CalendarRowView";

    public static final int PAST_ACTIVITY = 1;
    public static final int FUTURE_OR_CURRENT_ACTIVITY = 2;

    protected static final int TO_NEXT_WEEK = 1;
    protected static final int TO_PREVIOUS_WEEK = -1;

    protected int numActivities;
    protected boolean shouldDrawCircle;
    protected boolean isPastActivity;
    protected int numPreviousWeekActivities;
    protected int numNextWeekActivities;
    protected boolean shouldDrawLineToPreviousWeek;
    protected boolean shouldDrawLineToNextWeek;
    protected boolean shouldDrawTextView = true;

    protected final Drawable horizontalLine;
    protected final Drawable circle;
    protected final Drawable hollowCircle;

    protected int drawBoundsWidth;
    protected int drawBoundsHeight;
    protected int centerX;
    protected int centerY;

    public static CalendarRowView newZeroRowInstance(Context context)
    {
        return new CalendarHalfRowView(context, 0);
    }

    public static CalendarRowView newNormalRowInstance(Context context, int numActivities)
    {
        return new CalendarFullRowView(context, numActivities);
    }

    protected CalendarRowView(Context context, int numActivities)
    {
        super(context);
        this.numActivities = numActivities;

        horizontalLine = getResources().getDrawable(R.drawable.line);
        circle = getResources().getDrawable(R.drawable.calendar_circle);
        hollowCircle = getResources().getDrawable(R.drawable.calendar_hollow_circle);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);

        drawBoundsWidth = getWidth();
        drawBoundsHeight = convertToDrawBoundsHeight(getHeight());
        centerX = drawBoundsWidth / 2;
        centerY = drawBoundsHeight / 2;

        horizontalLine.setBounds(0, 0, drawBoundsWidth, drawBoundsHeight);

        final int circleDiameter = getResources().getDimensionPixelSize(R.dimen
                .calendar_circle_diameter);

        final int circleLeftOffsetPx = centerX - (circleDiameter / 2);
        final int circleTopOffsetPx = centerY - (circleDiameter / 2);

        circle.setBounds(circleLeftOffsetPx, circleTopOffsetPx, circleLeftOffsetPx + circleDiameter,
                circleTopOffsetPx + circleDiameter);
        hollowCircle.setBounds(circleLeftOffsetPx, circleTopOffsetPx,
                circleLeftOffsetPx + circleDiameter,
                circleTopOffsetPx + circleDiameter);

        //So the text are centered in the circle properly
        setIncludeFontPadding(false);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas)
    {
        horizontalLine.draw(canvas);

        if (shouldDrawCircle)
        {
            canvas.save();

            //draw outside bounds
            Rect clipBounds = canvas.getClipBounds();
            int drawOutsideBoundsAmount = getResources().getDimensionPixelSize(R.dimen
                    .calendar_row_view_draw_outside_bounds_amount);
            clipBounds.inset(-drawOutsideBoundsAmount, -drawOutsideBoundsAmount);
            canvas.clipRect(clipBounds, Region.Op.REPLACE);
            if (isPastActivity)
            {
                circle.draw(canvas);
                setTextColor(getResources().getColor(R.color.white));

                drawOrangeLine(TO_PREVIOUS_WEEK, canvas);
                if (shouldDrawLineToNextWeek)
                {
                    drawOrangeLine(TO_NEXT_WEEK, canvas);
                }
            }
            else //future or this weeks activity
            {
                hollowCircle.draw(canvas);
                setTextColor(getResources().getColor(R.color.black));
            }
            canvas.restore();

            if (shouldDrawTextView)
            {
                super.onDraw(canvas);
                bringToFront();
            }
        }
    }

    /* Setters */

    public CalendarRowView setCircle(final int activityType)
    {
        if (activityType == PAST_ACTIVITY)
        {
            this.isPastActivity = true;
        }
        else if (activityType == FUTURE_OR_CURRENT_ACTIVITY)
        {
            this.isPastActivity = false;
        }
        else
        {
            throw new IllegalArgumentException("Invalid activity type");
        }
        shouldDrawCircle = true;
        return this;
    }

    public CalendarRowView setDrawLineToPreviousWeek(int numPreviousWeekActivities)
    {
        shouldDrawLineToPreviousWeek = true;
        this.numPreviousWeekActivities = numPreviousWeekActivities;
        return this;
    }

    public CalendarRowView setDrawLineToNextWeek(int numNextWeekActivities)
    {
        shouldDrawLineToNextWeek = true;
        this.numNextWeekActivities = numNextWeekActivities;
        return this;
    }

    /* Class helper methods */

    protected int getLineThickness(int lineHorizontalDirection)
    {
        if (lineHorizontalDirection != TO_PREVIOUS_WEEK && lineHorizontalDirection != TO_NEXT_WEEK)
        {
            throw new IllegalArgumentException("Invalid direction");
        }

        return (lineHorizontalDirection == TO_PREVIOUS_WEEK)
                ? getLineThicknessToPreviousWeek() : getLineThicknessToNextWeek();
    }

    protected abstract int convertToDrawBoundsHeight(int height);

    protected abstract void drawOrangeLine(int lineHorizontalDirection, Canvas canvas);

    protected abstract int getLineThicknessToPreviousWeek();

    protected abstract int getLineThicknessToNextWeek();
}