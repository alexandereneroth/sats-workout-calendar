package se.greatbrain.sats.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
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
    private final boolean shouldDrawCircle;
    private final boolean isPastActivity;
    private final boolean isZeroRow;
    private final Drawable horizontalLine;
    private final Drawable circle;
    private final Drawable hollowCircle;
    private final Drawable lineToNext;
    private final Drawable lineToPrevious;

    private int drawBoundsWidth;
    private int drawBoundsHeight;
    private int centerX;
    private int centerY;

    private CalendarRowView(Context context, boolean shouldDrawCircle,
            boolean isPastActivity, boolean isZeroRow, boolean drawLineToPreviousWeek,
            int numPreviousWeekActivities, boolean drawLineToNextWeek, int numNextWeekActivities)
    {
        super(context);
        this.shouldDrawCircle = shouldDrawCircle;
        this.isPastActivity = isPastActivity;
        this.isZeroRow = isZeroRow;

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
                drawOrangeLineTowards(canvas, 320, -40);
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

        drawBoundsWidth = getWidth();
        drawBoundsHeight = isZeroRow ? getHeight() * 2 : getHeight();
        centerX = drawBoundsWidth / 2;
        centerY = drawBoundsHeight / 2;

        horizontalLine.setBounds(0, 0, drawBoundsWidth, drawBoundsHeight);
        lineToPrevious.setBounds(0, 0, drawBoundsWidth, drawBoundsHeight);

        int circleDiameter = getResources().getDimensionPixelSize(R.dimen.calendar_circle_diameter);

        int circleLeftOffsetPx = centerX - (circleDiameter / 2);
        int circleTopOffsetPx = centerY - (circleDiameter / 2);

        circle.setBounds(circleLeftOffsetPx, circleTopOffsetPx, circleLeftOffsetPx + circleDiameter,
                circleTopOffsetPx + circleDiameter);
        hollowCircle.setBounds(circleLeftOffsetPx, circleTopOffsetPx,
                circleLeftOffsetPx + circleDiameter,
                circleTopOffsetPx + circleDiameter);

    }

    private void drawOrangeLineTowards(Canvas canvas, int x, int y)
    {
        Paint linePaint = new Paint();
        linePaint.setColor(getResources().getColor(R.color.calendar_item));
        linePaint.setStrokeWidth(18);
        //TODO linePaint.setAntiAlias(true);
        canvas.drawLine(centerX, centerY, centerX + x, centerY + y, linePaint);
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
        private boolean drawCircle = false;
        private boolean isPastActivity;
        private boolean drawLineToPreviousWeek = false;
        private boolean drawLineToNextWeek = false;
        private boolean isZeroRow = false;
        private int numNextWeekActivities;
        private int numPreviousWeekActivities;

        private Builder() {}

        public Builder(Context context)
        {
            this.context = context;
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
            return new CalendarRowView(context, drawCircle, isPastActivity, isZeroRow,
                    drawLineToPreviousWeek, numPreviousWeekActivities, drawLineToNextWeek,
                    numNextWeekActivities);
        }

    }
}