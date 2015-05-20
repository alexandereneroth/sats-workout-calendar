package se.greatbrain.sats.util;

import android.content.Context;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

public class DimensionUtil
{
    public static Point getScreenDimensions(Context context)
    {
        final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        final Display display = wm.getDefaultDisplay();
        Point dimensions = new Point();
        display.getSize(dimensions);

        return dimensions;
    }

    /**
     * Shorten a line between two points, at its end
     * @param startPoint The lines starting point
     * @param endPoint The lines ending point
     * @param pixelCount The number of pixels which to shorten the line by
     */
    public static void shortenLine(Point startPoint, Point endPoint, float pixelCount)
    {
        final float pixelsToShortenBy = pixelCount * -1;
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
            { endPoint.y -= pixelsToShortenBy; }
            else
            { endPoint.y += pixelsToShortenBy; }
        }
        else if (dy == 0)
        {
            // horizontal line:
            if (endPoint.x < startPoint.x)
            { endPoint.x -= pixelsToShortenBy; }
            else
            { endPoint.x += pixelsToShortenBy; }
        }
        else
        {
            // non-horizontal, non-vertical line:
            double length = Math.sqrt(dx * dx + dy * dy);
            double scale = (length + pixelsToShortenBy) / length;
            dx *= scale;
            dy *= scale;
            endPoint.x = startPoint.x + (int) dx;
            endPoint.y = startPoint.y + (int) dy;
        }
    }

    public static int dpToPx(Context context, int dp)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, context.getResources().getDisplayMetrics());
    }
}
