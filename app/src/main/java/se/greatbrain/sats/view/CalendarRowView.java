package se.greatbrain.sats.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.widget.TextView;

import se.greatbrain.sats.R;

public class CalendarRowView extends TextView
{
    private final boolean shouldDrawCircle;
    private final boolean isPastActivity;
    private final Drawable horizontalLine;
    private final Drawable circle;
    private final Drawable hollowCircle;

    private static final int LINE_BOUNDS_RIGHT_DP = 200;
    private int lineBoundsBottomPx = 40;

    private static final int CIRCLE_LEFT_OFFSET_DP = 99;

    public CalendarRowView(Context context, int viewHeightInPx, boolean shouldDrawCircle,
            boolean isPastActivity)
    {
        super(context);
        this.shouldDrawCircle = shouldDrawCircle;
        this.isPastActivity = isPastActivity;

        lineBoundsBottomPx = viewHeightInPx;
        horizontalLine = getResources().getDrawable(R.drawable.line);
        circle = getResources().getDrawable(R.drawable.calendar_circle);
        hollowCircle = getResources().getDrawable(R.drawable.calendar_hollow_circle);

        final int rightPx = dpToPx(LINE_BOUNDS_RIGHT_DP);
        final int bottomPx = lineBoundsBottomPx;

        horizontalLine.setBounds(0, 0, rightPx, bottomPx);

        circle.setBounds(CIRCLE_LEFT_OFFSET_DP, -5, CIRCLE_LEFT_OFFSET_DP + getResources()
                        .getDimensionPixelSize(R.dimen.calendar_circle_radius),
                getResources().getDimensionPixelSize(R.dimen.calendar_circle_radius) - 5);
        hollowCircle.setBounds(CIRCLE_LEFT_OFFSET_DP, -5, CIRCLE_LEFT_OFFSET_DP + getResources()
                        .getDimensionPixelSize(R.dimen.calendar_circle_radius),
                getResources().getDimensionPixelSize(R.dimen.calendar_circle_radius) - 5);
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
            super.onDraw(canvas);
        }
    }

    private int dpToPx(int dp)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics());
    }
}