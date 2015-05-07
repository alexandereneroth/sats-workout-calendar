package se.greatbrain.sats.fragment;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import se.greatbrain.sats.view.CalendarRowView;
import se.greatbrain.sats.R;

public class CalendarColumnFragment extends Fragment
{
    private static final String TAG = "ScreenSlidePageFragment";
    //TODO set number of rows based on max activities in a week
    private static final int NUM_ROWS = 7;

    private float topFragmentHeight;
    private OnPageClickedListener listenerOnPageClicked_MainActivity;

    public interface OnPageClickedListener
    {
        void onPageClicked(int page);
    }

    //TODO use Eventbus instead of normal callback
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
        Resources r = getResources();

        topFragmentHeight = r.getDimension(R.dimen.calendar_height);

        LinearLayout rootView = (LinearLayout) inflater.inflate(R.layout.fragment_calendar_column,
                container, false);

        final int indexInAdapter = getArguments().getInt(CalendarFragment.CalendarPagerAdapter
                .ADAPTER_POSITION);

        if (indexInAdapter % 2 == 0)
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
                listenerOnPageClicked_MainActivity.onPageClicked(indexInAdapter);
            }
        });

        addTopRow(rootView);
        addRows(rootView);
        addHalfRow(rootView);
        addDateRow(rootView);

        return rootView;
    }

    private void addTopRow(LinearLayout rootView)
    {
        View topRow = new View(rootView.getContext());
        topRow.setBackgroundColor(getResources().getColor(R.color.green));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, getHeightOfOneColumnRow(NUM_ROWS));
        rootView.addView(topRow, params);
    }

    private void addRows(LinearLayout rootView)
    {
        // start at row 'rows' and add all rows through 1, the zero row will be added in daterow and
        // is not part of the number of rows
        for (int i = NUM_ROWS; i > 0; --i)
        {
            CalendarRowView row = new CalendarRowView(rootView.getContext(),
                    getHeightOfOneColumnRow(NUM_ROWS), true, false);
            row.setText(String.valueOf(i));
            row.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, getHeightOfOneColumnRow(NUM_ROWS));

            rootView.addView(row, params);
        }
    }

    private void addHalfRow(LinearLayout rootView)
    {
        View halfRow = new View(rootView.getContext());
        halfRow.setBackgroundColor(getResources().getColor(R.color.green));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, getHeightOfOneColumnRow(NUM_ROWS) / 2);
        rootView.addView(halfRow, params);
    }

    private void addDateRow(LinearLayout rootView)
    {
        TextView row = new TextView(rootView.getContext());
        row.setText("12-14/3");
        row.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        row.setBackground(getResources().getDrawable(R.drawable.calendar_column_row_date_bg));
        row.setPadding(0, 7, 0, 0);
        row.setGravity(Gravity.CENTER | Gravity.TOP);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, getHeightOfOneColumnRow(NUM_ROWS));

        rootView.addView(row, params);
    }

    private int getHeightOfOneColumnRow(int rows)
    {
        return (int) (topFragmentHeight / (rows + 2.5));
    }
}
