package se.greatbrain.sats.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import se.greatbrain.sats.ListGroup;
import se.greatbrain.sats.R;
import se.greatbrain.sats.adapter.WorkoutListAdapter;

public class WorkoutListFragment extends Fragment
{
    private static final String TAG_LOG = "WorkoutListFragment";
    private List<ListGroup> listGroups;
    private SparseArray<ListGroup> sparseGroups;

    public static WorkoutListFragment newInstance(ArrayList<ListGroup> listGroups)
    {
        WorkoutListFragment fragment = new WorkoutListFragment();

        Bundle args = new Bundle();
        args.putParcelableArrayList("listGroups", listGroups);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        listGroups = getArguments().getParcelableArrayList("listGroups");

        sparseGroups = new SparseArray<>();

        for (int i = 0; i < listGroups.size(); i++)
        {
            sparseGroups.append(i, listGroups.get(i));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_workout_list, container, false);

        StickyListHeadersListView listView = (StickyListHeadersListView) view.findViewById(
                R.id.expandable_list_view);
        WorkoutListAdapter adapter = new WorkoutListAdapter(getActivity(), sparseGroups);
        listView.setAdapter(adapter);

        return view;
    }
}
