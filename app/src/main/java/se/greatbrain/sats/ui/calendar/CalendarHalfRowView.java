package se.greatbrain.sats.ui.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import se.greatbrain.sats.R;
import se.greatbrain.sats.util.DimensionUtil;

public class CalendarHalfRowView extends CalendarRowView
{
    protected CalendarHalfRowView(Context context, int numActivities)
    {
        super(context, numActivities);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
        shouldDrawTextView = false;
    }

    @Override
    protected int convertToDrawBoundsHeight(int height)
    {
        return height * 2;
    }

    @Override
    protected int getLineThicknessToPreviousWeek()
    {
        int lineThickness = getResources().getDimensionPixelSize(R.dimen.calendar_line_thickness);
        if (numPreviousWeekActivities == 0)
        {
            return lineThickness / 2;
        }
        return lineThickness;
    }

    @Override
    protected int getLineThicknessToNextWeek()
    {
        int lineThickness = getResources().getDimensionPixelSize(R.dimen.calendar_line_thickness);
        if (numNextWeekActivities == 0)
        {
            return lineThickness / 2;
        }
        return lineThickness;
    }

    @Override
    protected void drawOrangeLine(int lineHorizontalDirection, Canvas canvas)
    {
        final int lineThickness = getLineThickness(lineHorizontalDirection);

        Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(getResources().getColor(R.color.calendar_item));
        linePaint.setStrokeWidth(lineThickness);

        final int numDestinationWeekActivities = (lineHorizontalDirection == CalendarRowView
                .TO_PREVIOUS_WEEK) ?
                numPreviousWeekActivities : numNextWeekActivities;
        final int deltaActivities = numActivities - numDestinationWeekActivities;
        final int deltaX = drawBoundsWidth * lineHorizontalDirection;
        final int deltaY = drawBoundsHeight * deltaActivities;

        final Point originPoint = new Point(0, 0);
        Point deltaPoint = new Point(deltaX, deltaY);

        final int shortenBy = getResources().getDimensionPixelSize(R.dimen
                .calendar_line_shorten_by);
        DimensionUtil.shortenLine(originPoint, deltaPoint, shortenBy);

        int originX = centerX;
        int originY = centerY;
        int destinationX = originX + deltaPoint.x;
        int destinationY = originY + deltaPoint.y;

        // Move the line up, so it doesn't overlap with the date row
        if (numDestinationWeekActivities == 0)
        {
            final int offsetY = lineThickness / 2;
            originY -= offsetY;
            destinationY -= offsetY;
        }

        canvas.drawLine(originX, originY, destinationX, destinationY, linePaint);
    }
}
