package se.greatbrain.sats.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class VerticalLayouter
{
    private final RelativeLayout viewGroup;

    private int lastViewId = 0;
    private int viewHeight;
    private boolean useWrapContent = true;

    public VerticalLayouter(RelativeLayout viewGroup)
    {
        this.viewGroup = viewGroup;
    }

    public VerticalLayouter addView(View view)
    {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                useWrapContent ? ViewGroup.LayoutParams.WRAP_CONTENT : viewHeight);
        if(lastViewId > 0)
        {
            params.addRule(RelativeLayout.BELOW, lastViewId);
        }
        view.setId(++lastViewId);

        viewGroup.addView(view, params);

        return this;
    }

    public VerticalLayouter addViews(View[] views)
    {
        for(View v : views)
        {
            addView(v);
        }
        return this;
    }

    public VerticalLayouter setViewHeight(int viewHeight)
    {
        this.viewHeight = viewHeight;
        useWrapContent = false;
        return this;
    }

}
