package se.greatbrain.sats;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.HashSet;

import de.greenrobot.event.EventBus;
import se.greatbrain.sats.event.JsonParseCompleteEvent;
import se.greatbrain.sats.event.ServerErrorEvent;
import se.greatbrain.sats.fragment.WorkoutListFragment;
import se.greatbrain.sats.ion.IonClient;

public class MainActivity extends ActionBarActivity
{
    private static final String TAG_LOG = "MainActivity";
    private MenuItem reloadButton;
    private WorkoutListFragment workoutListFragment;
    private HashSet<String> finishedJsonParseEvents = new HashSet<>();
    private SlidingMenu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadJsonDataFromWeb();
        EventBus.getDefault().register(this);

        FragmentManager manager = getFragmentManager();
        workoutListFragment = new WorkoutListFragment();
        manager.beginTransaction().add(R.id.bottom_fragment_container,
                workoutListFragment).commit();

        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
        menu.setShadowWidth(200);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.sliding_menu);

        findViewById(R.id.sliding_menu_find_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(v.getContext(), GoogleMapActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void loadJsonDataFromWeb()
    {
        Log.d(TAG_LOG, "loadJsonDataFromWeb");
        IonClient.getInstance(this).getAllData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setDisplayShowTitleEnabled(true);
//        actionBar.setIcon(R.drawable.sats_logo_menu);
//        actionBar.setHomeButtonEnabled(true);
//        actionBar.setDisplayShowHomeEnabled(true);


//        actionBar.setHomeAsUpIndicator(R.drawable.sats_logo_menu);

        //back to activity
//        actionBar.setDisplayHomeAsUpEnabled(true);
//
//        View actionBarView = getLayoutInflater().inflate(R.layout.action_bar_menu, null);
//        actionBar.setCustomView(actionBarView);
//        actionBar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);

        reloadButton = menu.findItem(R.id.action_bar_refresh_button);
        setupReloadItemMenu();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
//
        switch (item.getItemId())
        {
            case R.id.action_bar_menu_button:
            {
                menu.toggle();
                return true;
            }
            case R.id.action_bar_refresh_button:
            {
                setupReloadItemMenu();
                loadJsonDataFromWeb();
                return true;
            }

        }
        
        return super.onOptionsItemSelected(item);
    }

    private void setupReloadItemMenu()
    {
        Animation reloadAnimation = AnimationUtils.loadAnimation(this, R.anim.reload_rotate);
        reloadButton.setActionView(R.layout.action_bar_reloading);

        ImageView imageView = (ImageView) reloadButton.getActionView()
                .findViewById(R.id.action_bar_refresh_button_reloading);

        imageView.startAnimation(reloadAnimation);
    }

    public void onEventMainThread(JsonParseCompleteEvent event)
    {
        Log.d("jsonEvent", event.getSourceEvent());
        if (finishedJsonParseEvents.add(event.getSourceEvent()))
        {
            if (finishedJsonParseEvents.size() == 6)
            {
                finishedJsonParseEvents.clear();
                updateWorkoutListFragment();
            }
        }
    }

    public void onEventMainThread(ServerErrorEvent event)
    {
        Toast.makeText(this, event.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void updateWorkoutListFragment()
    {
        reloadButton.setActionView(null);
        workoutListFragment.refreshList();
    }
}
