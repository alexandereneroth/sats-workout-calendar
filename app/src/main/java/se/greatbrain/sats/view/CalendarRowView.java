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

    private final boolean shouldDrawCircle;
    private final boolean isPastActivity;
    private final boolean isZeroRow;
    private final Drawable horizontalLine;
    private final Drawable circle;
    private final Drawable hollowCircle;

    private CalendarRowView(Context context, boolean shouldDrawCircle,
            boolean isPastActivity, boolean isZeroRow)
    {
        super(context);
        this.shouldDrawCircle = shouldDrawCircle;
        this.isPastActivity = isPastActivity;
        this.isZeroRow = isZeroRow;

        horizontalLine = getResources().getDrawable(R.drawable.line);
        circle = getResources().getDrawable(R.drawable.calendar_circle);
        hollowCircle = getResources().getDrawable(R.drawable.calendar_hollow_circle);
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

    public static class RowBuilder
    {
        private Context context;
        private boolean drawCircle = false;
        private boolean isPastActivity;
        private boolean isZeroRow = false;

        private RowBuilder() {}

        public RowBuilder(Context context)
        {
            this.context = context;
        }

        public RowBuilder drawCircle(int activityType)
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

        public RowBuilder setIsZeroRow()
        {
            isZeroRow = true;
            return this;
        }


        public CalendarRowView build()
        {
            return new CalendarRowView(context, drawCircle, isPastActivity,
                    isZeroRow);
        }

    }
}