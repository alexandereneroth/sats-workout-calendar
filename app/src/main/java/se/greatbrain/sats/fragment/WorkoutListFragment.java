package se.greatbrain.sats.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import se.greatbrain.sats.R;
import se.greatbrain.sats.adapter.WorkoutListAdapter;
import se.greatbrain.sats.realm.RealmClient;

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

        listView = (StickyListHeadersListView) view.findViewById(
                R.id.expandable_list_view);

        adapter = new WorkoutListAdapter(getActivity(),
                RealmClient.getInstance(getActivity()).getAllActivitiesWithWeek());
        listView.setAdapter(adapter);
        return view;
    }

    public void refreshList()
    {
        adapter = new WorkoutListAdapter(getActivity(),
                RealmClient.getInstance(getActivity()).getAllActivitiesWithWeek());
        listView.setAdapter(adapter);
    }
}
