package se.greatbrain.sats.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.widget.TextView;

import se.greatbrain.sats.R;

public class CalendarRowView extends TextView
{
    public static final int PAST_ACTIVITY = 1;
    public static final int FUTURE_OR_CURRENT_ACTIVITY = 2;

    private static final String TAG = "CalendarRowView";
    private static final int TO_NEXT_WEEK = 1;
    private static final int TO_PREVIOUS_WEEK = -1;

    private final int numActivities;
    private final boolean shouldDrawCircle;
    private final boolean isPastActivity;
    private final boolean isZeroRow;
    private final int numPreviousWeekActivities;
    private final int numNextWeekActivities;
    private final boolean shouldDrawLineToPreviousWeek;
    private final boolean shouldDrawLineToNextWeek;
    private final Drawable horizontalLine;
    private final Drawable circle;
    private final Drawable hollowCircle;
    private final Drawable lineToNext;
    private final Drawable lineToPrevious;

    private int drawBoundsWidth;
    private int drawBoundsHeight;
    private int width;
    private int height;
    private int centerX;
    private int centerY;

    private CalendarRowView(Context context, int numActivities, boolean shouldDrawCircle,
            boolean isPastActivity, boolean isZeroRow, boolean drawLineToPreviousWeek,
            int numPreviousWeekActivities, boolean drawLineToNextWeek, int numNextWeekActivities)
    {
        super(context);
        this.numActivities = numActivities;
        this.shouldDrawCircle = shouldDrawCircle;
        this.isPastActivity = isPastActivity;
        this.isZeroRow = isZeroRow;
        this.shouldDrawLineToPreviousWeek = drawLineToPreviousWeek;
        this.shouldDrawLineToNextWeek = drawLineToNextWeek;
        this.numPreviousWeekActivities = numPreviousWeekActivities;
        this.numNextWeekActivities = numNextWeekActivities;

        horizontalLine = getResources().getDrawable(R.drawable.line);
        circle = getResources().getDrawable(R.drawable.calendar_circle);
        hollowCircle = getResources().getDrawable(R.drawable.calendar_hollow_circle);
        lineToNext = getResources().getDrawable(R.drawable.calendar_line);
        lineToPrevious = getResources().getDrawable(R.drawable.calendar_line);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        horizontalLine.draw(canvas);

        if (shouldDrawCircle)
        {
            canvas.save();
            //draw outside bounds
            Rect clipBounds = canvas.getClipBounds();
            clipBounds.inset(-200, -200);
            canvas.clipRect(clipBounds, Region.Op.REPLACE);
            if (isPastActivity)
            {
                circle.draw(canvas);
                setTextColor(getResources().getColor(R.color.white));
                drawOrangeLine(TO_PREVIOUS_WEEK, canvas);
                if (shouldDrawLineToNextWeek)
                {
                    drawOrangeLineToNextWeek(canvas);
                }
            }
            else
            {
                hollowCircle.draw(canvas);
                setTextColor(getResources().getColor(R.color.black));
            }
            canvas.restore();
            if (!isZeroRow) {super.onDraw(canvas);}
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        setIncludeFontPadding(false);

        super.onLayout(changed, left, top, right, bottom);

        int drawBoundsWidth = getWidth();
        int drawBoundsHeight = isZeroRow ? getHeight() * 2 : getHeight();
        int width = getWidth();
        int height = getHeight();
        int centerX = drawBoundsWidth / 2;
        int centerY = drawBoundsHeight / 2;

        horizontalLine.setBounds(0, 0, drawBoundsWidth, drawBoundsHeight);

        int circleDiameter = getResources().getDimensionPixelSize(R.dimen.calendar_circle_diameter);

        int circleLeftOffsetPx = centerX - (circleDiameter / 2);
        int circleTopOffsetPx = centerY - (circleDiameter / 2);

        circle.setBounds(circleLeftOffsetPx, circleTopOffsetPx, circleLeftOffsetPx + circleDiameter,
                circleTopOffsetPx + circleDiameter);
        hollowCircle.setBounds(circleLeftOffsetPx, circleTopOffsetPx,
                circleLeftOffsetPx + circleDiameter,
                circleTopOffsetPx + circleDiameter);

    }

    private void drawOrangeLine(int direction, Canvas canvas)
    {
        Paint linePaint = new Paint();
        linePaint.setColor(getResources().getColor(R.color.calendar_item));
        linePaint.setStrokeWidth(18);
        if (isZeroRow && numPreviousWeekActivities == 0)
        {
            linePaint.setStrokeWidth(9);
        }
        int deltaActivities = numActivities - numPreviousWeekActivities;
        int deltaX = drawBoundsWidth * direction;
        int deltaY = (-drawBoundsHeight * deltaActivities);

        Point originPoint = new Point(0, 0);
        Point deltaPoint = new Point(deltaX, deltaY);
        int cutoff = getResources().getDimensionPixelSize(R.dimen.calendar_line_cutoff);
        lengthenLine(originPoint, deltaPoint, -cutoff);

        if (isZeroRow && numPreviousWeekActivities == 0)
        {
            canvas.drawLine(centerX, centerY - 6, centerX + deltaPoint.x, centerY - deltaPoint.y -6,
                    linePaint);
        }else
        {
            canvas.drawLine(centerX, centerY, centerX + deltaPoint.x, centerY - deltaPoint.y,
                    linePaint);
        }
    }

    private void drawOrangeLineToNextWeek(Canvas canvas)
    {
        Paint linePaint = new Paint();
        linePaint.setColor(getResources().getColor(R.color.calendar_item));
        linePaint.setStrokeWidth(18);
        if (isZeroRow && numNextWeekActivities == 0)
        {
            linePaint.setStrokeWidth(9);
        }
        int deltaActivities = numNextWeekActivities - numActivities;
        int deltaX = drawBoundsWidth;
        int deltaY = (-drawBoundsHeight * deltaActivities);

        Point originPoint = new Point(0, 0);
        Point deltaPoint = new Point(deltaX, deltaY);
        int cutoff = getResources().getDimensionPixelSize(R.dimen.calendar_line_cutoff);
        lengthenLine(originPoint, deltaPoint, -cutoff);

        if (isZeroRow && numNextWeekActivities == 0)
        {
            canvas.drawLine(centerX, centerY -6, centerX + deltaPoint.x,
                    centerY + deltaPoint.y -6,
                    linePaint);
        }else
        {
            canvas.drawLine(centerX, centerY, centerX + deltaPoint.x, centerY + deltaPoint.y,
                    linePaint);
        }
    }

    public void lengthenLine(Point startPoint, Point endPoint, float pixelCount)
    {
        if (startPoint == endPoint)
        {
            return; // not a line
        }
        double dx = endPoint.x - startPoint.x;
        double dy = endPoint.y - startPoint.y;
        if (dx == 0)
        {
            // vertical line:
            if (endPoint.y < startPoint.y)
            { endPoint.y -= pixelCount; }
            else
            { endPoint.y += pixelCount; }
        }
        else if (dy == 0)
        {
            // horizontal line:
            if (endPoint.x < startPoint.x)
            { endPoint.x -= pixelCount; }
            else
            { endPoint.x += pixelCount; }
        }
        else
        {
            // non-horizontal, non-vertical line:
            double length = Math.sqrt(dx * dx + dy * dy);
            double scale = (length + pixelCount) / length;
            dx *= scale;
            dy *= scale;
            endPoint.x = startPoint.x + (int) dx;
            endPoint.y = startPoint.y + (int) dy;
        }
    }

    private int dpToPx(int dp)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics());
    }

    private double getAngleFromCenterTowards(int x, int y)
    {
        int adjacent = x - centerX;
        int opposite = y - centerY;
        int ratio = opposite / adjacent;
        double degrees = Math.atan(ratio);
        return degrees;
    }

    public static class Builder
    {
        private Context context;
        private int numActivities = 0; //TODO refactor away isZeroRow
        private boolean drawCircle = false;
        private boolean isPastActivity;
        private boolean drawLineToPreviousWeek = false;
        private boolean drawLineToNextWeek = false;
        private boolean isZeroRow = false;
        private int numNextWeekActivities;
        private int numPreviousWeekActivities;

        private Builder() {}

        public Builder(Context context, int numActivities)
        {
            this.context = context;
            this.numActivities = numActivities;
        }

        public Builder drawCircle(int activityType)
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
            this.drawCircle = true;
            return this;
        }

        public Builder setIsZeroRow()
        {
            isZeroRow = true;
            return this;
        }

        public Builder drawLineToPreviousWeek(int numPreviousWeekActivities)
        {
            this.drawLineToPreviousWeek = true;
            this.numPreviousWeekActivities = numPreviousWeekActivities;
            return this;
        }

        public Builder drawLineToNextWeek(int numNextWeekActivities)
        {
            this.drawLineToNextWeek = true;
            this.numNextWeekActivities = numNextWeekActivities;
            return this;
        }


        public CalendarRowView build()
        {
            return new CalendarRowView(context, numActivities, drawCircle, isPastActivity,
                    isZeroRow,
                    drawLineToPreviousWeek, numPreviousWeekActivities, drawLineToNextWeek,
                    numNextWeekActivities);
        }
    }
}