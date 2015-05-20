package se.greatbrain.sats.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import java.util.List;

import de.greenrobot.event.EventBus;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import se.greatbrain.sats.R;
import se.greatbrain.sats.data.ActivityWrapper;
import se.greatbrain.sats.data.RealmClient;
import se.greatbrain.sats.event.MyTrainingRefreshEvent;
import se.greatbrain.sats.event.WorkoutListScrollEvent;

public class WorkoutListFragment extends Fragment
{
    private static final String TAG_LOG = "WorkoutListFragment";
    private WorkoutListAdapter adapter;
    private StickyListHeadersListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_workout_list, container, false);

        listView = (StickyListHeadersListView) view.findViewById(R.id.expandable_list_view);
        EventBus.getDefault().register(this);

        refreshList();
        return view;
    }

    @Override
    public void onDestroy()
    {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void refreshList()
    {
        List<ActivityWrapper> activityWrappers = RealmClient.getInstance(getActivity())
                .getAllActivitiesWithWeek();
        adapter = new WorkoutListAdapter(getActivity(), activityWrappers);

        listView.setOnStickyHeaderChangedListener(new StickyListHeadersListView
                .OnStickyHeaderChangedListener()
        {
            @Override
            public void onStickyHeaderChanged(StickyListHeadersListView stickyListHeadersListView,
                    View view, int itemPosition, long headerId)
            {
                TextView trainingHeader = (TextView) getActivity().findViewById(R.id.training_list_headline);

                if (adapter.positionOfTodaysFirstItem() <= itemPosition)
                {
                    trainingHeader.setText("Kommande träning");
                }
                else
                {
                    trainingHeader.setText("Tidigare träning");
                }
            }
        });

        listView.setAdapter(adapter);
        listView.setSelectionFromTop(adapter.positionOfTodaysFirstItem(), 0);
    }

    public void onEvent(MyTrainingRefreshEvent event)
    {
        refreshList();
    }

    public void onEvent(WorkoutListScrollEvent event)
    {
        int position = adapter.getPositionFromWeekHash(event.weekHash);
        smoothScrollToPositionWithBugWorkAround(position, -2, 200);
    }

    private void smoothScrollToPositionWithBugWorkAround(final int position, final int offset,
            final int duration)
    {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final AbsListView view, int scrollState)
            {
                if (scrollState == SCROLL_STATE_IDLE)
                {
                    listView.setOnScrollListener(null);

                    listView.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            listView.smoothScrollToPositionFromTop(position, offset);
                        }
                    });
                }
            }

            @Override
            public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount,
                    final int totalItemCount) { }
        });

        listView.post(new Runnable() {
            @Override
            public void run()
            {
                listView.smoothScrollToPositionFromTop(position, offset, duration);
            }
        });
    }
}
