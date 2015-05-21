package se.greatbrain.sats.ui.calendar;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import se.greatbrain.sats.R;
import se.greatbrain.sats.util.DimensionUtil;

public class CalendarFullRowView extends CalendarRowView
{
    public CalendarFullRowView(Context context, int numActivities)
    {
        super(context, numActivities);
    }

    @Override
    protected int convertToDrawBoundsHeight(int height)
    {
        return height;
    }

    @Override
    protected int getLineThicknessToPreviousWeek()
    {
        return getResources().getDimensionPixelSize(R.dimen.calendar_line_thickness);
    }
    @Override
    protected int getLineThicknessToNextWeek()
    {
        return getResources().getDimensionPixelSize(R.dimen.calendar_line_thickness);
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

        final int originX = centerX;
        final int originY = centerY;
        final int destinationX = originX + deltaPoint.x;
        final int destinationY = originY + deltaPoint.y;
        canvas.drawLine(originX, originY, destinationX, destinationY, linePaint);
    }
}
