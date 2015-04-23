package se.greatbrain.sats;


import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import se.greatbrain.sats.fragment.WorkoutListFragment;
import se.greatbrain.sats.handler.ActivitiesResponseHandler;
import se.greatbrain.sats.handler.CentersResponseHandler;
import se.greatbrain.sats.handler.ClassCategoriesResponseHandler;
import se.greatbrain.sats.handler.ClassTypesResponseHandler;
import se.greatbrain.sats.handler.InstructorsResponseHandler;
import se.greatbrain.sats.handler.TypeResponseHandler;


public class MainActivity extends ActionBarActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setupRealm();

        ArrayList<ListGroup> groups = generateData();

        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().add(R.id.bottom_fragment_container,
                                       WorkoutListFragment.newInstance(groups)).commit();
    }

    private void setupRealm()
    {
        CentersResponseHandler centersResponseHandler = new CentersResponseHandler(this);
        centersResponseHandler.get();

        InstructorsResponseHandler instructorsResponseHandler = new InstructorsResponseHandler(this);
        instructorsResponseHandler.get();

        ClassCategoriesResponseHandler classCategoriesResponseHandler = new ClassCategoriesResponseHandler(this);
        classCategoriesResponseHandler.get();

        ClassTypesResponseHandler classTypesResponseHandler = new ClassTypesResponseHandler(this);
        classTypesResponseHandler.get();

        ActivitiesResponseHandler activitiesResponseHandler = new ActivitiesResponseHandler(this);
        activitiesResponseHandler.get();

        TypeResponseHandler typeResponseHandler = new TypeResponseHandler(this);
        typeResponseHandler.get();

    }

    private ArrayList<ListGroup> generateData()
    {
        ArrayList<ListGroup> groups = new ArrayList<>();
        List<String> items;

        for (int i = 0; i < 5; i++)
        {
            items = new ArrayList<>();

            for (int l = 1; l < 6; l++)
            {
                items.add("List item " + i);
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

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        View actionBarView = getLayoutInflater().inflate(R.layout.action_bar_menu, null);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        actionBar.setCustomView(actionBarView);
        actionBar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);

        return true;
    }

}
