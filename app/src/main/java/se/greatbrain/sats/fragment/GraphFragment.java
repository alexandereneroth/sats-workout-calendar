package se.greatbrain.sats.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.greatbrain.sats.R;

public class GraphFragment extends Fragment
{
    public static final int NUM_PAGES = 100;
    public static final int NUM_SIMULTANEOUS_PAGES = 5;
    private static final String TAG = "MainActivity";

    public ViewPager mPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        mPager = (ViewPager) view.findViewById(R.id.pager);

        PagerAdapter pagerAdapter = new ScreenSlidePagerAdapter( getFragmentManager() );

        // It is recommended to load twice, or three times the number of simultaneous pages
        mPager.setOffscreenPageLimit( NUM_SIMULTANEOUS_PAGES * 2 );

        mPager.setAdapter( pagerAdapter );
        mPager.setCurrentItem((NUM_PAGES/2)-(NUM_SIMULTANEOUS_PAGES/2), false);

        return view;
    }

    public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter
    {
        public static final String ITEM_INDEX = "item_index";

        public ScreenSlidePagerAdapter (FragmentManager fm)
        {
            super( fm );
        }

        @Override
        public android.support.v4.app.Fragment getItem (int i)
        {
            GraphColumnFragment fragment = new GraphColumnFragment();
            Bundle bundle = new Bundle( i );
            bundle.putInt( ITEM_INDEX, i );
            fragment.setArguments( bundle );

            return fragment;
        }

        @Override
        public int getCount ()
        {
            return NUM_PAGES;
        }

    /* For multiple pages */

        @Override
        public float getPageWidth (int position)
        {
            return 1f / NUM_SIMULTANEOUS_PAGES;
        }
    }
}
