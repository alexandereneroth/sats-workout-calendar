package se.greatbrain.sats.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import se.greatbrain.sats.ActivityWrapper;
import se.greatbrain.sats.ListGroup;
import se.greatbrain.sats.R;
import se.greatbrain.sats.adapter.WorkoutListAdapter;
import se.greatbrain.sats.realm.RealmClient;

public class WorkoutListFragment extends Fragment
{
    private static final String TAG_LOG = "WorkoutListFragment";
    private List<ListGroup> listGroups;
    private SparseArray<ListGroup> sparseGroups;

    public static WorkoutListFragment newInstance()
    {
        WorkoutListFragment fragment = new WorkoutListFragment();

        return fragment;
    }

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

        StickyListHeadersListView listView = (StickyListHeadersListView) view.findViewById(
                R.id.expandable_list_view);

        List<ActivityWrapper> activityWrappers = RealmClient.getInstance(getActivity()).getAllActivitiesWithWeek();

        final WorkoutListAdapter adapter = new WorkoutListAdapter(getActivity(), activityWrappers);
        listView.setAdapter(adapter);

        listView.smoothScrollToPosition(10);

        return view;
    }
}
