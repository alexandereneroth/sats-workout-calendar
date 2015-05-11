package se.greatbrain.sats.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import se.greatbrain.sats.R;
import se.greatbrain.sats.util.PixelUtil;

public class CalendarFullRowView extends CalendarRowView
{
    public CalendarFullRowView(Context context, int numActivities,
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
    protected int convertToDrawBoundsHeight(int height)
    {
        return height;
    }

    protected int getLineThicknessToPreviousWeek()
    {
        return getResources().getDimensionPixelSize(R.dimen.calendar_line_thickness);
    }

    protected int getLineThicknessToNextWeek()
    {
        return getResources().getDimensionPixelSize(R.dimen.calendar_line_thickness);
    }

    @Override
    protected void drawOrangeLine(int direction, Canvas canvas)
    {
        Paint linePaint = new Paint();
        linePaint.setColor(getResources().getColor(R.color.calendar_item));
        linePaint.setStrokeWidth(lineThicknessToPreviousWeek);
        int deltaActivities = numActivities - numPreviousWeekActivities;
        int deltaX = drawBoundsWidth * direction;
        int deltaY = (-drawBoundsHeight * deltaActivities);

        Point originPoint = new Point(0, 0);
        Point deltaPoint = new Point(deltaX, deltaY);
        int cutoff = getResources().getDimensionPixelSize(R.dimen.calendar_line_cutoff);
        PixelUtil.lengthenLine(originPoint, deltaPoint, -cutoff);

        canvas.drawLine(centerX, centerY, centerX + deltaPoint.x, centerY - deltaPoint.y,
                linePaint);
    }

    @Override
    protected void drawOrangeLineToNextWeek(Canvas canvas)
    {
        Paint linePaint = new Paint();
        linePaint.setColor(getResources().getColor(R.color.calendar_item));
        linePaint.setStrokeWidth(lineThicknessToNextWeek);
        int deltaActivities = numNextWeekActivities - numActivities;
        int deltaX = drawBoundsWidth;
        int deltaY = (-drawBoundsHeight * deltaActivities);

        Point originPoint = new Point(0, 0);
        Point deltaPoint = new Point(deltaX, deltaY);
        int cutoff = getResources().getDimensionPixelSize(R.dimen.calendar_line_cutoff);
        PixelUtil.lengthenLine(originPoint, deltaPoint, -cutoff);

        canvas.drawLine(centerX, centerY, centerX + deltaPoint.x, centerY + deltaPoint.y,
                linePaint);
    }
}
