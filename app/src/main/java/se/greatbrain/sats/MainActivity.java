package se.greatbrain.sats;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import se.greatbrain.sats.fragment.WorkoutListFragment;
import se.greatbrain.sats.ion.IonClient;
import se.greatbrain.sats.model.realm.TrainingActivity;
import se.greatbrain.sats.realm.RealmClient;

public class MainActivity extends ActionBarActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<ListGroup> dummyListGroups = generateDummyListGroups();
        setupRealm();

        RealmClient client = new RealmClient(this);
        client.getAllActivitiesWithWeek();

        ArrayList<ListGroup> groups = generateDummyListGroups();

        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().add(R.id.bottom_fragment_container,
                WorkoutListFragment.newInstance(dummyListGroups)).commit();
    }

    private void setupRealm()
    {
        IonClient client = IonClient.getInstance(this);
        client.getAllData();
    }

    private ArrayList<ListGroup> generateDummyListGroups()
    {
        ArrayList<ListGroup> groups = new ArrayList<>();
        List<ActivityType> items;

        for (int i = 0; i < 10; i++)
        {
            items = new ArrayList<>();

            for (int l = 0; l < 5; l++)
            {
                items.add(ActivityType.getWithId(l % 3));
            }

            groups.add(new ListGroup("Grupp " + i, items));
        }

        return groups;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);

        View actionBarView = getLayoutInflater().inflate(R.layout.action_bar_menu, null);
        actionBar.setCustomView(actionBarView);
        actionBar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);

        return true;
    }

}
