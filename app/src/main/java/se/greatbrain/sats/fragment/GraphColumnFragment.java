package se.greatbrain.sats.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import se.greatbrain.sats.R;
import se.greatbrain.sats.activity.MainActivity;

public class GraphColumnFragment extends Fragment
{

    private OnPageClickedListener listenerOnPageClicked_MainActivity;

    public interface OnPageClickedListener
    {
        void onPageClicked (int page);
    }

    private static final String TAG = "ScreenSlidePageFragment";

    @Override
    public void onAttach (Activity activity)
    {
        super.onAttach( activity );

        try
        {
            listenerOnPageClicked_MainActivity = (OnPageClickedListener) activity;
        } catch (ClassCastException e)
        {
            throw new ClassCastException( activity.toString()
                    + " must implement" + OnPageClickedListener.class.getName() );
        }
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        RelativeLayout rootView = (RelativeLayout) inflater.inflate( R.layout
                        .fragment_graph_column,
                container, false );
        final int myAdapterIndex = getArguments().getInt( GraphFragment.ScreenSlidePagerAdapter
                .ITEM_INDEX );

        if (myAdapterIndex % 2 == 0)
        {
            rootView.setBackgroundColor( getResources().getColor( R.color.gray_dark ) );
        }

        rootView.setOnClickListener( new View.OnClickListener()
        {

            @Override
            public void onClick (View v)
            {
                listenerOnPageClicked_MainActivity.onPageClicked( myAdapterIndex );
            }
        } );

        int idToPlaceNextViewBelow = addRows( rootView, 4 );

        addDateRow(idToPlaceNextViewBelow, rootView);
        return rootView;
    }


    private int addRows (RelativeLayout rootView, int rows)
    {
        int idToPlaceNextViewBelow = R.id.graph_week_text;
        for (int i = 0; i < rows; ++i)
        {
            TextView row = new TextView( rootView.getContext() );
            row.setText( "Row" + i );
            row.setBackground(getResources().getDrawable(R.drawable.graph_column_bg));
            row.setTextColor( getResources().getColor( R.color.abc_primary_text_material_light ) );

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( ViewGroup.LayoutParams
                    .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
            params.addRule( RelativeLayout.BELOW, idToPlaceNextViewBelow );

            int id = i + 1;
            row.setId( id );
            idToPlaceNextViewBelow = id;

            rootView.addView( row, params );
        }
        return idToPlaceNextViewBelow;
    }

    private void addDateRow (int idToPlaceBelow, RelativeLayout rootView)
    {
        TextView row = new TextView( rootView.getContext() );
        row.setText("12-14/3");
        row.setBackgroundColor( getResources().getColor( R.color
                .bright_foreground_disabled_material_dark ) );
        row.setTextColor( getResources().getColor( R.color.material_deep_teal_200 ) );

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        params.addRule( RelativeLayout.BELOW, idToPlaceBelow);

        rootView.addView( row, params );
    }
}
