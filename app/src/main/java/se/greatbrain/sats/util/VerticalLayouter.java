package se.greatbrain.sats.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * A convenience class that can be used to easily layout views vertically in a RelativeLayout.
 * Newly placed views are placed below previously placed views that were placed by the same
 * VertialLayouter instance.
 */
public class VerticalLayouter
{
    private final RelativeLayout viewGroup;

    private int lastViewId = 0;
    private int viewHeight;
    private boolean useWrapContent = true;

    private VerticalLayouter(RelativeLayout viewGroup)
    {
        this.viewGroup = viewGroup;
    }

    /**
     * Sets the <code>viewGroup</code> argument as the ViewGroup to layout views in,
     * and returns a <code>VerticalLayouter</code> instance
     * @param viewGroup The ViewGroup to layout views in
     * @return A new instance of VerticalLayouter
     */
    public static VerticalLayouter layoutIn(RelativeLayout viewGroup)
    {
        return new VerticalLayouter(viewGroup);
    }

    /**
     * Place a view below the view that were previously placed by this VerticalLayouter instance.
     * (If any)
     * @param view The view to place
     * @return <code>this</code>
     */
    public VerticalLayouter placeView(View view)
    {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (useWrapContent ? ViewGroup.LayoutParams.WRAP_CONTENT : viewHeight)
        );

        //Because the first view placed in 'viewGroup' should not be placed below anything
        if(lastViewId > 0)
        {
            params.addRule(RelativeLayout.BELOW, lastViewId);
        }

        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);

        view.setId(++lastViewId);

        viewGroup.addView(view, params);

        return this;
    }

    public VerticalLayouter placeViews(View[] views)
    {
        for(View v : views)
        {
            placeView(v);
        }
        return this;
    }

    public VerticalLayouter useViewHeight(int viewHeight)
    {
        this.viewHeight = viewHeight;
        useWrapContent = false;
        return this;
    }

}
