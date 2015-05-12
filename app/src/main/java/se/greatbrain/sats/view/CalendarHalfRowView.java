package se.greatbrain.sats.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import se.greatbrain.sats.R;
import se.greatbrain.sats.util.PixelUtil;

public class CalendarHalfRowView extends CalendarRowView
{
    protected CalendarHalfRowView(Context context, int numActivities,
            boolean shouldDrawCircle, boolean isPastActivity,
            boolean drawLineToPreviousWeek, int numPreviousWeekActivities,
            boolean drawLineToNextWeek,
            int numNextWeekActivities)
    {
        super(context, numActivities, shouldDrawCircle, isPastActivity,
                drawLineToPreviousWeek, numPreviousWeekActivities, drawLineToNextWeek,
                numNextWeekActivities);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
        shouldDrawTextView = false;
        drawBoundsHeight = getHeight() * 2;
    }

    @Override
    protected int convertToDrawBoundsHeight(int height)
    {
        return height * 2;
    }

    @Override
    protected void drawOrangeLine(int direction, Canvas canvas)
    {
        int lineThickness = lineThicknessToPreviousWeek;

        Paint linePaint = new Paint();
        linePaint.setColor(getResources().getColor(R.color.calendar_item));
        linePaint.setStrokeWidth(lineThickness);
        int deltaActivities = numActivities - numPreviousWeekActivities;
        int deltaX = drawBoundsWidth * direction;
        int deltaY = (-drawBoundsHeight * deltaActivities);

        Point originPoint = new Point(0, 0);
        Point deltaPoint = new Point(deltaX, deltaY);
        int cutoff = getResources().getDimensionPixelSize(R.dimen.calendar_line_cutoff);
        PixelUtil.shortenLine(originPoint, deltaPoint, cutoff);

        // so the line doesn't overlap with the daterow
        final int originOffsetY = (int)(lineThickness /2);
        if (numPreviousWeekActivities == 0)
        {

            canvas.drawLine(
                    centerX,
                    centerY - originOffsetY,
                    centerX + deltaPoint.x,
                    centerY - deltaPoint.y - originOffsetY,
                    linePaint);
        }
        else
        {
            canvas.drawLine(
                    centerX,
                    centerY,
                    centerX + deltaPoint.x,
                    centerY - deltaPoint.y,
                    linePaint);
        }
    }

    protected void drawOrangeLineToNextWeek(Canvas canvas)
    {
        int lineThickness = lineThicknessToNextWeek;

        Paint linePaint = new Paint();
        linePaint.setColor(getResources().getColor(R.color.calendar_item));
        linePaint.setStrokeWidth(lineThickness);
        int deltaActivities = numNextWeekActivities - numActivities;
        int deltaX = drawBoundsWidth;
        int deltaY = (-drawBoundsHeight * deltaActivities);

        Point originPoint = new Point(0, 0);
        Point deltaPoint = new Point(deltaX, deltaY);
        int cutoff = getResources().getDimensionPixelSize(R.dimen.calendar_line_cutoff);
        PixelUtil.shortenLine(originPoint, deltaPoint, cutoff);

        // so the line doesn't overlap with the daterow
        final int originOffsetY = (int)(lineThickness /2);
        if (numNextWeekActivities == 0)
        {
            canvas.drawLine(
                    centerX,
                    centerY - originOffsetY,
                    centerX + deltaPoint.x,
                    centerY + deltaPoint.y - originOffsetY,
                    linePaint);
        }
        else
        {
            canvas.drawLine(
                    centerX,
                    centerY,
                    centerX + deltaPoint.x,
                    centerY + deltaPoint.y,
                    linePaint);
        }
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
}
