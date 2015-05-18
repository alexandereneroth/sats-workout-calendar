package se.greatbrain.sats.ui.pager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.greatbrain.sats.R;
import se.greatbrain.sats.ui.pager.calendar.CalendarFragment;
import se.greatbrain.sats.ui.pager.tab.TabLayout;

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

        TabLayout tabs = (TabLayout) view.findViewById(R.id.sliding_tabs);

        tabs.setCustomTabColorizer(new TabLayout.TabColorizer() {
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
