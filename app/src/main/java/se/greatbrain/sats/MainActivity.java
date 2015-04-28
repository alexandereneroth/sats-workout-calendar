package se.greatbrain.sats;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import de.greenrobot.event.EventBus;
import se.greatbrain.sats.event.ServerErrorEvent;
import se.greatbrain.sats.fragment.WorkoutListFragment;
import se.greatbrain.sats.ion.IonClient;
import se.greatbrain.sats.realm.RealmClient;

public class MainActivity extends ActionBarActivity
{
    private static final String TAG_LOG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupRealm();
        EventBus.getDefault().register(this);

        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().add(R.id.bottom_fragment_container,
                WorkoutListFragment.newInstance()).commit();
    }

    @Override
    protected void onDestroy()
    {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void setupRealm()
    {
        IonClient.getInstance(this).getAllData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);

        View actionBarView = getLayoutInflater().inflate(R.layout.action_bar_menu, null);
        actionBar.setCustomView(actionBarView);
        actionBar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);

        return true;
    }

    public void onEventMainThread(ServerErrorEvent event)
    {
        Toast.makeText(this, event.getMessage(), Toast.LENGTH_LONG).show();
    }

}
