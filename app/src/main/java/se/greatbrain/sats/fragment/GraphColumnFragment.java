package se.greatbrain.sats.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import se.greatbrain.sats.R;

public class GraphColumnFragment extends Fragment
{
    private OnPageClickedListener listenerOnPageClicked_MainActivity;

    public interface OnPageClickedListener
    {
        void onPageClicked(int page);
    }

    private static final String TAG = "ScreenSlidePageFragment";

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        try
        {
            listenerOnPageClicked_MainActivity = (OnPageClickedListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement" + OnPageClickedListener.class.getName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        LinearLayout rootView = (LinearLayout) inflater.inflate(R.layout
                        .fragment_graph_column,
                container, false);
        final int myAdapterIndex = getArguments().getInt(GraphFragment.ScreenSlidePagerAdapter
                .ITEM_INDEX);

        if (myAdapterIndex % 2 == 0)
        {
            rootView.setBackgroundColor(getResources().getColor(R.color.primary_calendar));
        }
        else
        {
            rootView.setBackgroundColor(getResources().getColor(R.color.secondary_calendar));
        }

        rootView.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                listenerOnPageClicked_MainActivity.onPageClicked(myAdapterIndex);
            }
        });

        int idToPlaceNextViewBelow = addRows(rootView, 4);

        addDateRow(idToPlaceNextViewBelow, rootView);
        return rootView;
    }


    private int addRows(LinearLayout rootView, int rows)
    {
        int idToPlaceNextViewBelow = R.id.graph_week_text;
        for (int i = 0; i < rows; ++i)
        {
            TextView row = new TextView(rootView.getContext());
            row.setText(String.valueOf(i));
            row.setTextColor(getResources().getColor(R.color.white));
            row.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            int id = i + 1;
            row.setId(id);
            idToPlaceNextViewBelow = id;
            params.weight = 1;

            rootView.addView(row, params);
        }
        return idToPlaceNextViewBelow;
    }

    private void addDateRow(int idToPlaceBelow, LinearLayout rootView)
    {
        TextView row = new TextView(rootView.getContext());
        row.setText("12-14/3");
        row.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        row.setBackground(getResources().getDrawable(R.drawable.graph_column_bg));
        row.setPadding(0, 17, 0, 0);
        row.setGravity(Gravity.CENTER | Gravity.TOP);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, 100);//TODO check what ___ 100 is. dp? sp? in? px?

        rootView.addView(row, params);
    }
}
