package se.greatbrain.sats.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.greenrobot.event.EventBus;
import se.greatbrain.sats.R;
import se.greatbrain.sats.event.ScrollEvent;
import se.greatbrain.sats.tabs.SlidingTabLayout;

public class TopViewPagerFragment extends Fragment
{
    private CalendarFragment graphFragment = new CalendarFragment();
    private static final int NUMBER_OF_TABS = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.top_fragment_view_pager, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager_container);
        viewPager.setAdapter(new PagerAdapter(getFragmentManager()));

        SlidingTabLayout tabs = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);

        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position)
            {
                return getResources().getColor(R.color.pink);
            }
        });
        tabs.setDistributeEvenly(true);
        tabs.setViewPager(viewPager);

        return view;
    }

    private final class PagerAdapter extends FragmentPagerAdapter
    {
        public PagerAdapter (FragmentManager fm)
        {
            super( fm );
        }

        @Override
        public android.support.v4.app.Fragment getItem (int position)
        {
            if (position == 0)
            {
                return graphFragment;
            }
            else
            {
                return new CalendarFragment();
            }
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            if (position == 0)
            {
                return "Kalender";
            }
            else
            {
                return "Statistik";
            }
        }

        @Override
        public int getCount ()
        {
            return NUMBER_OF_TABS;
        }

    }
}
