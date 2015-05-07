package se.greatbrain.sats.activity;

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
import se.greatbrain.sats.R;
import se.greatbrain.sats.fragment.GraphColumnFragment;
import se.greatbrain.sats.fragment.GraphFragment;
import se.greatbrain.sats.event.IonCallCompleteEvent;
import se.greatbrain.sats.fragment.WorkoutListFragment;
import se.greatbrain.sats.ion.IonClient;

public class MainActivity extends ActionBarActivity implements GraphColumnFragment.OnPageClickedListener
{
    private static final String TAG = "MainActivity";
    private MenuItem reloadButton;
    private WorkoutListFragment workoutListFragment;
    private GraphFragment graphFragment;
    private SlidingMenu slidingMenu;
    private HashSet<String> finishedIonCalls = new HashSet<>();
    private boolean errorMessageNotShown = true;
    private int numberOfErrors = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadJsonDataFromWeb();
        EventBus.getDefault().register(this);

        FragmentManager manager = getFragmentManager();
        android.support.v4.app.FragmentManager supportManager = getSupportFragmentManager();

        workoutListFragment = new WorkoutListFragment();
        graphFragment = new GraphFragment();
        supportManager.beginTransaction()
                .add(R.id.top_fragment_container, graphFragment)
                .add(R.id.bottom_fragment_container, workoutListFragment)
                .commit();

        setupSlidingMenu();
    }

    @Override
    protected void onDestroy()
    {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void loadJsonDataFromWeb()
    {
        Log.d(TAG, "loadJsonDataFromWeb");
        IonClient.getInstance(this).getAllData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);

        View actionBarView = getLayoutInflater().inflate(R.layout.action_bar_menu, null);
        actionBar.setCustomView(actionBarView);
        actionBar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);

        // remove left actionbar padding
        android.support.v7.widget.Toolbar parent = (android.support.v7.widget.Toolbar)
                actionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        reloadButton = menu.findItem(R.id.action_bar_refresh_button);
        setupReloadItemMenu();

        ImageView menuIcon = (ImageView) findViewById(R.id.btn_dots_logo_sats_menu);
        menuIcon.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        slidingMenu.toggle();
                    }
                });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.action_bar_refresh_button)
        {
            setupReloadItemMenu();
            loadJsonDataFromWeb();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupReloadItemMenu()
    {
        Animation reloadAnimation = AnimationUtils.loadAnimation(this, R.anim.reload_rotate);

        if(finishedIonCalls.size() == 0 && numberOfErrors < 6)
        {
            reloadButton.setActionView(R.layout.action_bar_reloading);
            ImageView imageView = (ImageView) reloadButton.getActionView()
                    .findViewById(R.id.action_bar_refresh_button_reloading);
            imageView.startAnimation(reloadAnimation);
            numberOfErrors = 0;
        }
        else
        {
            numberOfErrors = 0;
        }
    }

    public void onEventMainThread(IonCallCompleteEvent event)
    {
        if (event.getSourceEvent().contains("error"))
        {
            numberOfErrors++;

            if (errorMessageNotShown)
            {
                Toast.makeText(this, "Server connection failed, please refresh",
                        Toast.LENGTH_LONG).show();
                errorMessageNotShown = false;
            }
        }

        if (finishedIonCalls.add(event.getSourceEvent()))
        {
            if (finishedIonCalls.size() == 6)
            {
                finishedIonCalls.clear();
                errorMessageNotShown = true;
                updateWorkoutListFragment();
            }
        }
    }
    
    private void updateWorkoutListFragment()
    {
        if (reloadButton != null)
        {
            reloadButton.setActionView(null);
            workoutListFragment.refreshList();
        }
    }


    private void setupSlidingMenu()
    {
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
        slidingMenu.setShadowWidth(200);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.sliding_menu);

        findViewById(R.id.sliding_menu_find_center).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(v.getContext(), GoogleMapActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onPageClicked (int page)
    {
        graphFragment.mPager.setCurrentItem(page - (graphFragment.NUM_SIMULTANEOUS_PAGES / 2),
                true);
        Log.d(TAG, "Page: " + page);

    }
}
