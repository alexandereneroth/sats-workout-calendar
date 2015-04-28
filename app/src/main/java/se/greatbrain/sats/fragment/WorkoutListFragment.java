package se.greatbrain.sats.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import se.greatbrain.sats.ActivityWrapper;
import se.greatbrain.sats.ListGroup;
import se.greatbrain.sats.R;
import se.greatbrain.sats.adapter.WorkoutListAdapter;
import se.greatbrain.sats.event.RealmUpdateCompleteEvent;

public class WorkoutListFragment extends Fragment
{
    private static final String TAG_LOG = "WorkoutListFragment";
    private List<ListGroup> listGroups;
    private SparseArray<ListGroup> sparseGroups;
    private WorkoutListAdapter adapter;
    private List<ActivityWrapper> activityWrappers;
    private StickyListHeadersListView listView;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_workout_list, container, false);

        listView = (StickyListHeadersListView) view.findViewById(
                R.id.expandable_list_view);

        activityWrappers = new ArrayList<ActivityWrapper>();
        adapter = new WorkoutListAdapter(getActivity(), activityWrappers);
        listView.setAdapter(adapter);
        return view;
    }

    public void onEvent(RealmUpdateCompleteEvent event)
    {
        Log.d("realm", event.getSourceEvent());
        adapter = new WorkoutListAdapter(getActivity(), event.getActivityWrappers());
        listView.setAdapter(adapter);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
