package se.greatbrain.sats.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SlidingDrawer;

import java.util.concurrent.atomic.AtomicBoolean;

import se.greatbrain.sats.R;
import se.greatbrain.sats.activity.GoogleMapActivity;
import se.greatbrain.sats.activity.MainActivity;

public class DrawerMenuListener extends DrawerLayout.SimpleDrawerListener implements ListView
        .OnItemClickListener
{
    private final Activity activity;
    private int position;
    private boolean itemClicked = false;
    private DrawerLayout drawerLayout;

    public DrawerMenuListener(Activity activity)
    {
        this.activity = activity;
        this.drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        this.position = position;
        itemClicked = true;
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onDrawerClosed(View drawerView)
    {
        if(GoogleMapActivity.wasBackPressed)
        {
            GoogleMapActivity.wasBackPressed = false;
        }

        if (itemClicked)
        {
            switch (position)
            {
                case 0:
                    Intent mainIntent = new Intent(drawerView.getContext(), MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    drawerView.getContext().startActivity(mainIntent);
                    if(activity.getClass() != MainActivity.class)
                    {
                        activity.finish();
                    }
                    break;
                case 1:
                    Intent mapIntent = new Intent(drawerView.getContext(), GoogleMapActivity.class);
                    mapIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    drawerView.getContext().startActivity(mapIntent);
                    if(activity.getClass() != GoogleMapActivity.class)
                    {
                        activity.finish();
                    }
                    break;
            }

            itemClicked = false;
        }
    }
}
