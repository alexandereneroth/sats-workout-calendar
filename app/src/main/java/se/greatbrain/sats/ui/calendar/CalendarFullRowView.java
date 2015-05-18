package se.greatbrain.sats.ui.calendar;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import se.greatbrain.sats.R;
import se.greatbrain.sats.util.PixelUtil;

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
    protected void drawOrangeLine(int direction, Canvas canvas)
    {
        int lineThickness = getLineThickness(direction);
        Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(getResources().getColor(R.color.calendar_item));
        linePaint.setStrokeWidth(lineThickness);

        int numDestinationWeekActivities = (direction == CalendarRowView.TO_PREVIOUS_WEEK) ?
                numPreviousWeekActivities : numNextWeekActivities;
        int deltaActivities = numActivities - numDestinationWeekActivities;
        int deltaX = drawBoundsWidth * direction;
        int deltaY = drawBoundsHeight * deltaActivities;

        Point originPoint = new Point(0, 0);
        Point deltaPoint = new Point(deltaX, deltaY);

        final int shortenBy = getResources().getDimensionPixelSize(R.dimen
                .calendar_line_shorten_by);
        PixelUtil.shortenLine(originPoint, deltaPoint, shortenBy);

        final int originX = centerX;
        final int originY = centerY;
        final int destinationX = originX + deltaPoint.x;
        final int destinationY = originY + deltaPoint.y;
        canvas.drawLine(originX, originY, destinationX, destinationY, linePaint);
    }
}
