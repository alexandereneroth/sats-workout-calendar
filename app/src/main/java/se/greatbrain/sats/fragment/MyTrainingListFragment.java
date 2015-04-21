package se.greatbrain.sats.fragment;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import se.greatbrain.sats.ListGroup;
import se.greatbrain.sats.R;

public class MyTrainingListFragment extends ListFragment {

    private List<ListGroup> listGroups;

    public static MyTrainingListFragment newInstance(ArrayList<ListGroup> listGroup)
    {
        MyTrainingListFragment fragment = new MyTrainingListFragment();

        Bundle args = new Bundle();
        args.putParcelableArrayList("listGroup", listGroup);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //listGroups = getArguments().getParcelableArrayList("listGroup");

        //setListAdapter(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_workout_list, container, false);
    }
}
